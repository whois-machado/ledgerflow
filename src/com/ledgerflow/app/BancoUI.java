package com.ledgerflow.app;

import com.ledgerflow.enums.TipoChavePix;
import com.ledgerflow.enums.TipoTransacao;
import com.ledgerflow.model.ContaBancaria;
import com.ledgerflow.exceptions.InsufficientFundsException;
import com.ledgerflow.model.Transacao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


/**
 * Camada de Interface do Usuário (CLI) para gerenciamento de operações bancárias.
 * Responsável pela interação direta, validação de inputs e exibição de dados.
 */
public class BancoUI {

    private final Scanner sc;

    /**
     * Construtor da classe BancoUI.
     * @param sc Objeto Scanner para captura de entradas do sistema.
     */
    public BancoUI(Scanner sc){
        this.sc = sc;
    }

    // --- MÉTODOS DE SESSÃO E AUTENTICAÇÃO ---

    /**
     * Gerencia o fluxo de login validando o CPF no repositório de contas.
     * @param mapaContas Mapa contendo a base de dados de contas do sistema.
     * @return O objeto ContaBancaria autenticado ou null caso não encontrado.
     */
    public ContaBancaria efetuarLogin(Map<String, ContaBancaria> mapaContas) {

        String cpfBusca = lerStringSegura("Digite seu CPF para acessar: ");

        ContaBancaria conta = mapaContas.get(cpfBusca);

        if (conta == null) {
            System.err.println("CPF não encontrado!");
        } else {
            System.out.println("Acesso autorizado para o CPF: " + cpfBusca);
        }
        return conta;
    }

    // --- MÉTODOS DE MAPEAMENTO DE OPÇAO ---

    /**
     * Converte a entrada numérica do menu no respectivo valor do Enum TipoTransacao.
     * @param op Código numérico escolhido pelo usuário.
     * @return O Enum correspondente ou null para representar "Todos".
     */
    private TipoTransacao mapearOpcaoParaTipoTransacao(int op){
        return switch(op){
            case 2 -> TipoTransacao.SAQUE;
            case 3 -> TipoTransacao.DEPOSITO;
            case 4 -> TipoTransacao.PIX;
            default -> null;
        };
    }

    /**
     * Converte a opção numérica do menu no Enum correspondente ao tipo de chave PIX.
     * @param op Opção selecionada pelo usuário.
     * @return O Enum TipoChavePix correspondente.
     */
    private TipoChavePix mapearOpcaoParaTipoChave(int op){
        return switch(op){
            case 1 -> TipoChavePix.CPF;
            case 2 -> TipoChavePix.EMAIL;
            case 3 -> TipoChavePix.TELEFONE;
            default -> TipoChavePix.ALEATORIA;
        };
    }

    // --- MÉTODOS DE FILTRAGEM ---

    /**
     * Filtra a lista de transações por categoria e período utilizando Java Streams.
     * @param extrato Lista completa de transações da conta.
     * @param tipo Categoria da transação (SAQUE, DEPOSITO, etc).
     * @param dias Janela retroativa de dias para o filtro.
     * @return Lista de transações que atendem aos critérios.
     */
    public List<Transacao> filtrarExtrato(List<Transacao> extrato, TipoTransacao tipo, int dias){

        LocalDateTime dataLimite = LocalDateTime.now().minusDays(dias);
        return extrato.stream()
                .filter(t-> tipo == null || t.getTipo() == tipo)
                .filter(t -> t.getDataHora().isAfter(dataLimite))
                .toList();
    }

    // --- MÉTODOS PARA VALIDAÇÃO DE INPUTS ---

    /**
     * Captura um inteiro de forma segura, tratando erros de formato e limites.
     * @param mensagem Texto a ser exibido ao usuário.
     * @param min Valor mínimo aceitável.
     * @param max Valor máximo aceitável.
     * @return O valor inteiro validado.
     */
    public int lerIntSeguro(String mensagem, int min, int max) {
        while (true) {
            try {
                System.out.print(mensagem);
                String entrada = sc.next();
                int valor = Integer.parseInt(entrada);

                if (valor < min || valor > max) {
                    System.err.println("Opção inválida! Escolha um número entre " + min + " e " + max + ".");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                System.err.println("Erro: Entrada inválida! Por favor, digite apenas números.");
            }
        }
    }

    /**
     * Captura um valor decimal positivo de forma segura.
     * @param mensagem Texto a ser exibido ao usuário.
     * @return O valor double validado.
     */
    public double lerDoubleSeguro(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                double valor = Double.parseDouble(sc.next());
                if (valor <= 0) {
                    System.err.println("Erro: O valor deve ser positivo.");
                    continue;
                }
                return valor;
            }
            catch (NumberFormatException e){
                System.err.println("Erro: Entrada inválida! Digite apenas números.");
            }
        }
    }

    /**
     * Captura uma String e remove espaços extras, impedindo campos vazios.
     * @param mensagem Texto a ser exibido ao usuário.
     * @return A String tratada.
     */
    public String lerStringSegura(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = sc.next().trim(); // Remove espaços extras
            if (entrada.isEmpty()) {
                System.err.println("Erro: Este campo não pode ser vazio.");
                continue;
            }
            return entrada;
        }
    }

    /**
     * Valida a integridade da conta destino para operações de transferência.
     * Verifica se a conta existe no sistema e impede transações para a mesma conta de origem.
     * @param origem Conta que está enviando os fundos.
     * @param destino Conta localizada via busca (CPF, Chave PIX, etc).
     * @return true se a conta destino for válida e apta a receber a operação.
     */
    private boolean validarDestinoOperacao(ContaBancaria origem, ContaBancaria destino){
        if(destino == null){
            System.err.println("[!] Erro: Chave PIX não encontrada no sistema.");
            return false;
        }
        if(destino.equals(origem)){
            System.err.println("[!] Erro: Não é permitido enviar PIX para si mesmo.");
            return false;
        }
        return true;
    }


    // --- MÉTODOS PARA EXECUTAR OPERAÇÕES BANCÁRIAS ---

    /**
     * Orquestra a operação de saque, tratando exceções de negócio e entrada.
     * @param conta Conta na qual o saque será efetuado.
     */
    public void realizarSaque(ContaBancaria conta){

        double vSaq = lerDoubleSeguro("Quanto deseja sacar: ");
        try{
            conta.sacar(vSaq);
            System.out.println("Saque realizado com sucesso!");
        }
        catch(InsufficientFundsException e){
            System.err.println("AVISO: " + e.getMessage());
        }
        catch(IllegalArgumentException e){
            System.err.println("ERRO DE ENTRADA: " + e.getMessage());
        }
        catch(Exception e){
            System.err.println("ERRO CRÍTICO: " + e.getMessage());
        }
    }

    /**
     * Orquestra o depósito de valores na conta bancária.
     * @param conta Conta destino do depósito.
     */
    public void realizarDeposito(ContaBancaria conta){

        double vDep = lerDoubleSeguro("Quanto deseja depositar: ");
        try{
            conta.depositar(vDep);
            System.out.println("Depósito de R$ " + vDep + " realizado com sucesso!");
        }
        catch(IllegalArgumentException e){
            System.err.println("ERRO DE VALOR: " + e.getMessage());
        }
        catch(Exception e){
            System.err.println("ERRO INESPERADO: " + e.getMessage());
        }
    }

    /**
     * Orquestra o envio de valores via PIX.
     * Utiliza a validação genérica de destino para garantir a segurança da transação.
     * @param contaLogada A conta autenticada que realizará a operação.
     * @param dicionarioChaves O mapeamento global de chaves PIX para contas bancárias.
     */
    public void realizarPix(ContaBancaria contaLogada, Map<String, ContaBancaria> dicionarioChaves) {

        System.out.println("\n--- ENVIAR PIX ---");
        System.out.println("[1] CPF | [2] E-mail | [3] Telefone | [4] Chave Aleatória");

        int op = lerIntSeguro("Selecione o tipo de chave: ", 1, 4);
        TipoChavePix tipoSelecionado = mapearOpcaoParaTipoChave(op);

        String chave = lerStringSegura("Digite o " + tipoSelecionado + " de destino: ");
        ContaBancaria contaDestino = dicionarioChaves.get(chave);

        if (!validarDestinoOperacao(contaLogada, contaDestino)) {
            return;
        }

        double valor = lerDoubleSeguro("Valor do PIX: R$ ");
        try{
            contaLogada.transferir(valor, contaDestino);
            System.out.println("PIX enviado com sucesso para: " + contaDestino.getCpfTitular());
        }
        catch (InsufficientFundsException e){
            System.err.println("AVISO: " + e.getMessage());
        }
        catch (IllegalArgumentException e){
            System.err.println("ERRO DE VALOR: " + e.getMessage());
        }
        catch (Exception e){
            System.err.println("ERRO INESPERADO: " + e.getMessage());
        }
    }


    // --- MÉTODOS PARA EXIBIÇÃO DE INTERFACES E TELAS ---

    /**
     * Exibe o menu de boas-vindas e captura a opção de acesso.
     * @return A opção de login selecionada.
     */
    public int exibirMenuLogin(){
        System.out.println("\n--- ACESSO AO SISTEMA ---");
        System.out.println("[1] Acessar Conta (Login)");
        System.out.println("[0] Sair do Sistema");

        return lerIntSeguro("Escolha: ", 0, 1);
    }

    /**
     * Exibe as funcionalidades disponíveis para a conta autenticada.
     * @param contaLogada Dados da conta atualmente logada.
     * @return A opção de funcionalidade selecionada.
     */
    public int exibirMenuConta(ContaBancaria contaLogada){
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("Logado como: " + contaLogada.getCpfTitular());
        System.out.println("[1] Consultar Saldo");
        System.out.println("[2] Depósito");
        System.out.println("[3] Saque");
        System.out.println("[4] Transferência");
        System.out.println("[5] Extrato");
        System.out.println("[6] Logout");

        return lerIntSeguro("Escolha: ", 1, 6);
    }

    /**
     * Exibe o saldo atual da conta logada.
     * @param contaLogada Dados da conta para consulta.
     */
    public void exibirSaldoConta(ContaBancaria contaLogada){
        System.out.println("Saldo atual: R$ " + contaLogada.getSaldo() + " reais");
    }

    /**
     * Orquestra a captura de filtros e exibe o extrato customizado.
     * @param contaLogada Conta de onde os dados do extrato serão extraídos.
     */
    public void exibirExtratoFiltrado(ContaBancaria contaLogada){
        System.out.println("\n--- FILTRO DE EXTRATO ---");
        System.out.println("[1] Tudo | [2] Saques | [3] Depósitos | [4] Transferências");
        int op = lerIntSeguro("Tipo: ", 1, 4);
        int dias = lerIntSeguro("Período (dias atrás, 0 para total): ", 0, 360);

        TipoTransacao tipo = mapearOpcaoParaTipoTransacao(op);
        List<Transacao> resultado = filtrarExtrato(contaLogada.getTransacoes(), tipo, dias);

        if (resultado.isEmpty()) {
            System.out.println("Nenhuma movimentação encontrada.");
        } else {
            resultado.forEach(t -> System.out.println(contaLogada.formatarTransacao(t, contaLogada.getNumero())));
        }
    }
}
