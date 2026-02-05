package com.ledgerflow.model;

import com.ledgerflow.enums.TipoConta;

public class ContaPoupanca extends ContaBancaria{

    public ContaPoupanca(String cpfTitular, String numero, String agencia, double saldoInicial){
        super(cpfTitular, numero, agencia, saldoInicial, TipoConta.POUPANCA);
    }

    public void aplicarRendimento(double taxa){
        if(taxa > 0){
            double saldoAtual = getSaldo();
            double rendimento = saldoAtual * taxa;
            depositar(rendimento);
        }
        else{
            throw new IllegalArgumentException("Taxa deve ser maior que zero");
        }
    }

    @Override
    public boolean sacar(double valor){

        return super.sacar(valor);
    }
}
