package com.ledgerflow.app;

import com.ledgerflow.enums.TipoConta;
import com.ledgerflow.model.ContaBancaria;
import com.ledgerflow.model.Transacao;

import java.util.Scanner;

public class AppBanco {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        ContaBancaria conta = new ContaBancaria(
                "123.456.789-00",
                "111",
                "0001",
                TipoConta.CORRENTE
        );
        int count = 0;

        int opcao = -1;
        while (opcao != 0) {
            System.out.print("\n");
            System.out.println("=== MENU BANCO ===");
            System.out.println("[1] - Mostrar Saldo");
            System.out.println("[2] - Depositar");
            System.out.println("[3] - Sacar");
            System.out.println("[4] - Transferir");
            System.out.println("[5] - Mostrar extrato");
            System.out.println("[0] - Sair");
            System.out.print("Escolha: ");

            opcao = sc.nextInt();

            switch (opcao) {
                case 1:
                    System.out.println("=== SALDO ===");
                    System.out.print("\n\n");
                    System.out.println("R$ " + conta.getSaldo()  + " reais");
                    break;
                case 2:
                    System.out.println("=== DEPÓSITO ===");
                    System.out.print("\n\n");
                    System.out.print("Quanto deseja depositar: ");
                    double vDep = sc.nextDouble();
                    conta.depositar(vDep);
                    break;
                case 3:
                    System.out.println("=== SAQUE ===");
                    System.out.print("\n\n");
                    System.out.println("Saldo de R$ " + conta.getSaldo() + " reais\n");
                    System.out.print("Quanto deseja sacar: ");
                    double vSaq = sc.nextDouble();
                    conta.sacar(vSaq);
                    break;
                case 4:
                    System.out.println("=== TRANSFERÊNCIA ===");
                    System.out.print("Número da conta destino: ");
                    String numDest = sc.next();

                    // por enquanto, cria uma conta destino “fictícia”
                    ContaBancaria contaDestino = new ContaBancaria(
                            "000.000.000-00",
                            numDest,
                            "0001",
                            TipoConta.CORRENTE
                    );

                    System.out.print("Valor da transferência: ");
                    double vTransf = sc.nextDouble();

                    boolean ok = conta.transferir(vTransf, contaDestino);
                    if (!ok) {
                        System.out.println("Falha na transferência (valor inválido ou saldo insuficiente).");
                    } else {
                        System.out.println("Transferência realizada com sucesso.");
                    }
                    break;

                case 5:
                    System.out.print("\n\n");
                    for (Transacao t : conta.getTransacoes()) {
                        System.out.println(conta.exibirExtrato(t, conta.getNumero()));
                    }
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }

        sc.close();
    }
}
