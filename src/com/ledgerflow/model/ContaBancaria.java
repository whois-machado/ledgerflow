package com.ledgerflow.model;

import com.ledgerflow.enums.TipoConta;
import com.ledgerflow.enums.TipoTransacao;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ContaBancaria {

    private final String cpfTitular;
    private final String numero;
    private final String agencia;
    protected double saldo;
    private final TipoConta tipoConta;
    private List<Transacao> transacoes = new ArrayList<>();

    public ContaBancaria(String cpfTitular, String numero, String agencia, double saldoInicial, TipoConta tipoConta){
        this.cpfTitular = cpfTitular;
        this.numero = numero;
        this.agencia = agencia;
        this.saldo = saldoInicial;
        this.tipoConta = tipoConta;
    }

    public ContaBancaria(String cpfTitular, String numero, String agencia, TipoConta tipoConta){
        this(cpfTitular, numero, agencia, 0.0, tipoConta);
    }

    public String getCpfTitular(){
        return cpfTitular;
    }

    public TipoConta getTipoConta(){
        return tipoConta;
    }

    public String getNumero(){
        return numero;
    }

    public String getAgencia(){
        return agencia;
    }

    public double getSaldo(){
        return saldo;
    }

    public List<Transacao> getTransacoes() {
        return transacoes;
    }


    //  exibir o extrato:

    DateTimeFormatter fmtData = DateTimeFormatter.ofPattern("dd/MM/yy");
    DateTimeFormatter fmtHora = DateTimeFormatter.ofPattern("HH:mm");

    public String exibirExtrato(Transacao trans, String numerodaContaOrigem){

        String data = trans.getDataHora().format(fmtData);
        String hora = trans.getDataHora().format(fmtHora);

        // SAQUE
        if (trans.getTipo() == TipoTransacao.SAQUE) {
            return "DATA: " + data + " | HORARIO: " + hora + " - SAQUE de R$ " + trans.getValor();
        }

        // DEPOSITO
        if (trans.getTipo() == TipoTransacao.DEPOSITO) {
            return "DATA: " + data + " | HORARIO: " + hora + " - DEPÓSITO de R$ " + trans.getValor();
        }

        // TRANSFERENCIA
        if (numerodaContaOrigem.equals(trans.getNumeroContaOrigem())) {
            return "DATA: " + data + " | HORARIO: " + hora + " - TRANSFERÊNCIA feita para conta " + trans.getNumeroContaDestino() + " | Valor: R$ " + trans.getValor();
        } else {
            return "DATA: " + data + " | HORARIO: " + hora + " - TRANSFERÊNCIA recebida da conta " + trans.getNumeroContaOrigem() + " | Valor: R$ " + trans.getValor();
        }
    }

    // Métodos de negócio que controlam mudanças de saldo:

    public boolean depositar(double valor){
        if(valor <= 0){
            return false;
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
        return true;
    }

    public boolean sacar(double valor){

        if(valor <= 0 || valor > saldo){
            System.out.println("Saque inválido!\nvalor = " + valor + ", saldo = " + saldo);
            return false;
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
        return true;
    }

    public boolean transferir(double valor, ContaBancaria contaDestino){
        if(valor <= 0 || valor > this.saldo){
            return false;
        }

        this.saldo -= valor;
        contaDestino.saldo += valor;

        // extrato na conta origem
        Transacao trans = new Transacao(
                UUID.randomUUID().toString(),
                this.getNumero(), // numero da contaOrigem
                contaDestino.getNumero(), // numero da contaDestino
                valor,
                TipoTransacao.TRANSFERENCIA,
                LocalDateTime.now()
        );
        this.transacoes.add(trans);
        contaDestino.getTransacoes().add(trans);

        return true;
    }
}
