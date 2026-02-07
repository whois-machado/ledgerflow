package com.ledgerflow.app;

import com.ledgerflow.model.ContaBancaria;
import com.ledgerflow.exceptions.InsufficientFundsException;
import com.ledgerflow.model.Transacao;

import java.util.InputMismatchException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BancoUI {

    private final Scanner sc;
    public BancoUI(Scanner sc){
        this.sc = sc;
    }

    public ContaBancaria realizarLogin(Map<String, ContaBancaria> mapaContas) {

        String cpfBusca = lerStringSegura("Digite seu CPF para acessar: ");

        ContaBancaria conta = mapaContas.get(cpfBusca);

        if (conta == null) {
            System.err.println("CPF não encontrado!");
        } else {
            System.out.println("Acesso autorizado para o CPF: " + cpfBusca);
        }
        return conta;
    }

    // metodos para validar a entrada do user(int, double e String)

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

    // metodos para executar (Saque, Deposito ou Transferencia)

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

    public void realizarTransferencia(ContaBancaria contaLogada, Map<String, ContaBancaria> mapaContas) {

        String cpfDest = lerStringSegura("CPF da conta destino: ");

        ContaBancaria contaDestino = mapaContas.get(cpfDest);

        if (contaDestino == null){
            System.err.println("ERRO: Conta destino não encontrada.");
        }
        else if (contaDestino == contaLogada){
            System.err.println("ERRO: Você não pode transferir para si mesmo.");
        }
        else{
            double valor = lerDoubleSeguro("Valor da transferência: ");
            try{
                contaLogada.transferir(valor, contaDestino);
                System.out.println("Transferência realizada com sucesso!");
            }
            catch (Exception e){
                System.err.println("ERRO INESPERADO: " + e.getMessage());
            }
        }
    }

    // metodos para exibir uma interface(Login, Menu ou Saldo)

    public int exibirMenuLogin(){
        System.out.println("\n--- ACESSO AO SISTEMA ---");
        System.out.println("[1] Acessar Conta (Login)");
        System.out.println("[0] Sair do Sistema");

        return lerIntSeguro("Escolha: ", 0, 1);
    }

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

    public void exibirSaldoConta(ContaBancaria contaLogada){
        System.out.println("Saldo atual: R$ " + contaLogada.getSaldo() + " reais");
    }

    public void exibirExtrato(ContaBancaria contaLogada){
        if (contaLogada.getTransacoes().isEmpty()){
            System.out.println("Nenhuma transação realizada até o momento.");
        }
        else{
            for (Transacao t : contaLogada.getTransacoes()) {
                System.out.println(contaLogada.formatarTransacao(t, contaLogada.getNumero()));
            }
        }
    }
}
