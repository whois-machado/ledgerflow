package com.ledgerflow.model;

import com.ledgerflow.enums.TipoConta;
import com.ledgerflow.enums.TipoTransacao;
import com.ledgerflow.exceptions.InsufficientFundsException;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.function.Predicate;


/**
 * Representa a entidade base de uma conta bancária no ecossistema LedgerFlow.
 * Esta classe gerencia o estado financeiro, validações de negócio e o
 * log transacional imutável.
 *  @author Gabriel Machado
 * @version 3.0 (Fase de Data API e Qualidade)
 */
public class ContaBancaria {

    private final String cpfTitular;
    private final String numero;
    private final String agencia;
    protected double saldo;
    private final TipoConta tipoConta;
    private List<Transacao> transacoes = new ArrayList<>();

    /**
     * Construtor completo para inicialização de conta com saldo customizado.
     * * @param cpfTitular Identificador único do proprietário.
     * @param numero Número identificador da conta.
     * @param agencia Código da agência vinculada.
     * @param saldoInicial Valor de abertura da conta.
     * @param tipoConta Categoria da conta (CORRENTE/POUPANCA).
     */
    public ContaBancaria(String cpfTitular, String numero, String agencia, double saldoInicial, TipoConta tipoConta){
        this.cpfTitular = cpfTitular;
        this.numero = numero;
        this.agencia = agencia;
        this.saldo = saldoInicial;
        this.tipoConta = tipoConta;
    }

    /**
     * Construtor simplificado para abertura de conta com saldo zerado.
     */
    public ContaBancaria(String cpfTitular, String numero, String agencia, TipoConta tipoConta){
        this(cpfTitular, numero, agencia, 0.0, tipoConta);
    }

    /** @return O CPF do titular da conta para fins de identificação. */
    public String getCpfTitular(){
        return cpfTitular;
    }

    /** @return O tipo da conta (Enum TipoConta) para aplicação de regras de negócio específicas. */
    public TipoConta getTipoConta(){
        return tipoConta;
    }

    /** @return O número da conta, utilizado como chave em operações transacionais. */
    public String getNumero(){
        return numero;
    }

    /** @return O código da agência para localização da conta no sistema. */
    public String getAgencia(){
        return agencia;
    }

    /** @return O saldo atualizado da conta após as operações de débito e crédito. */
    public double getSaldo(){
        return saldo;
    }

    /** @return A lista imutável de transações realizadas (Log Transacional). */
    public List<Transacao> getTransacoes() {
        return transacoes;
    }


    //  exibir o extrato:

    DateTimeFormatter fmtData = DateTimeFormatter.ofPattern("dd/MM/yy");
    DateTimeFormatter fmtHora = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Traduz um objeto Transacao em uma representação textual amigável para o usuário.
     * Identifica automaticamente a direção da transferência (enviada vs recebida).
     *  @param trans Objeto de transação a ser formatado.
     * @param numContaOrigem Número da conta do usuário logado para comparação lógica.
     * @return String formatada contendo data, hora, tipo e valor da operação.
     */
    public String formatarTransacao(Transacao trans, String numContaOrigem){

        String data = trans.getDataHora().format(fmtData);
        String hora = trans.getDataHora().format(fmtHora);

        // SAQUE
        if (trans.getTipo() == TipoTransacao.SAQUE) {
            return "DATA: " + data + " | HORARIO: " + hora + " - SAQUE de R$ " + trans.getValor();
        }

        // DEPOSITO
        else if (trans.getTipo() == TipoTransacao.DEPOSITO) {
            return "DATA: " + data + " | HORARIO: " + hora + " - DEPÓSITO de R$ " + trans.getValor();
        }

        // TRANSFERENCIA
        else if (numContaOrigem.equals(trans.getNumeroContaOrigem()) && trans.getTipo() == TipoTransacao.PIX) {
            return "DATA: " + data + " | HORARIO: " + hora + " - TRANSFERÊNCIA feita para conta " + trans.getNumeroContaDestino() + " | Valor: R$ " + trans.getValor();
        } else {
            return "DATA: " + data + " | HORARIO: " + hora + " - TRANSFERÊNCIA recebida da conta " + trans.getNumeroContaOrigem() + " | Valor: R$ " + trans.getValor();
        }
    }

    // Métodos de negócio que controlam mudanças de saldo:

    /**
     * Incrementa o capital da conta.
     * Valida a positividade do valor e registra a entrada no log transacional.
     *  @param valor Quantia a ser depositada.
     * @throws IllegalArgumentException Se o valor for menor ou igual a zero.
     */
    public void depositar(double valor){
        if(valor <= 0){
            throw new IllegalArgumentException("O valor do depósito deve ser positivo.");
        }

        saldo += valor;
        Transacao trans = new Transacao(
                UUID.randomUUID().toString(),
                getNumero(),
                null,
                valor,
                TipoTransacao.DEPOSITO,
                LocalDateTime.now()
        );

        transacoes.add(trans);
    }

    /**
     * Realiza o débito de capital da conta.
     * Implementa blindagem contra saldo insuficiente e valores inválidos.
     *  @param valor Quantia a ser sacada.
     * @throws IllegalArgumentException Se o valor for negativo.
     * @throws InsufficientFundsException Se o valor exceder o saldo disponível.
     */
    public void sacar(double valor){

        if(valor <= 0){
            throw new IllegalArgumentException("O valor do saque deve ser positivo.");
        }
        if(valor > this.saldo){
            throw new InsufficientFundsException("Saldo Insuficiente.");
        }

        saldo -= valor;
        Transacao trans = new Transacao(
                UUID.randomUUID().toString(),
                getNumero(),
                null,
                valor,
                TipoTransacao.SAQUE,
                LocalDateTime.now()
        );

        transacoes.add(trans);
    }

    /**
     * Executa uma transferência atômica entre contas.
     * Garante que a origem seja debitada e o destino creditado simultaneamente.
     * Possui cláusula de guarda contra auto-transferência (prevenção de loop).
     *  @param valor Quantia a transferir.
     * @param contaDestino Objeto da conta que receberá o crédito.
     * @throws InsufficientFundsException Se a conta de origem não tiver saldo.
     * @throws IllegalArgumentException Se houver tentativa de transferir para a própria conta.
     */
    public void transferir(double valor, ContaBancaria contaDestino) throws InsufficientFundsException{

        if(this.equals(contaDestino)){
            throw new IllegalArgumentException("Uma conta não pode transferir valores para si mesma.");
        }

        if(valor <= 0){
            throw new IllegalArgumentException("O valor deve ser maior que zero.");
        }

        this.sacar(valor);
        contaDestino.depositar(valor);

        // extrato na conta origem
        Transacao trans = new Transacao(
                UUID.randomUUID().toString(),
                this.getNumero(), // numero da contaOrigem
                contaDestino.getNumero(), // numero da contaDestino
                valor,
                TipoTransacao.PIX,
                LocalDateTime.now()
        );
        this.transacoes.add(trans);
        contaDestino.getTransacoes().add(trans);
    }

    /**
     * Data API: Filtra o histórico de transações com base em critérios dinâmicos.
     * @param tipo Filtro por TipoTransacao (null para todos)
     * @param inicio Data inicial do intervalo (null para sem limite inferior)
     * @param fim Data final do intervalo (null para sem limite superior)
     * @return Lista de transações que atendem aos critérios.
     */
    public List<Transacao> buscarExtratoInteligente(TipoTransacao tipo, LocalDateTime inicio, LocalDateTime fim) {
        return transacoes.stream()
                .filter(t -> tipo == null || t.getTipo() == tipo)
                .filter(t -> inicio == null || !t.getDataHora().isBefore(inicio))
                .filter(t -> fim == null || !t.getDataHora().isAfter(fim))
                .collect(Collectors.toList());
    }

}
