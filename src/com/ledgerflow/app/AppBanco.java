package com.ledgerflow.app;

import com.ledgerflow.model.ContaBancaria;
import com.ledgerflow.model.ContaCorrente;
import com.ledgerflow.model.ContaPoupanca;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AppBanco {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        BancoUI ui = new BancoUI(sc);
        Map<String, ContaBancaria> mapaContas = new HashMap<>();
        ContaBancaria c1 = new ContaCorrente("12345678900", "001", "1010", 500.0);
        mapaContas.put(c1.getCpfTitular(), c1);

        ContaBancaria c2 = new ContaPoupanca("98765432100", "002", "2020", 1000.0);
        mapaContas.put(c2.getCpfTitular(), c2);

        ContaBancaria contaLogada = null;
        boolean rodando = true;

        System.out.println("=== BEM-VINDO AO LEDGERFLOW ===");
        while(rodando){
            if(contaLogada == null){
                int opLogin = ui.exibirMenuLogin();

                if(opLogin == 0){
                    rodando = false;
                    continue;
                }
                contaLogada = ui.efetuarLogin(mapaContas);
            }
            else{
                int opMenu = ui.exibirMenuConta(contaLogada);

                switch (opMenu) {
                    case 1:
                        ui.exibirSaldoConta(contaLogada);
                        break;
                    case 2:
                        ui.realizarDeposito(contaLogada);
                        break;
                    case 3:
                        ui.realizarSaque(contaLogada);
                        break;
                    case 4:
                        ui.realizarPix(contaLogada, mapaContas);
                        break;
                    case 5:
                        ui.exibirExtratoFiltrado(contaLogada);
                        break;
                    case 6:
                        contaLogada = null;
                        System.out.println("Sessão encerrada.");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            }
        }
sc.close();
    }
}

