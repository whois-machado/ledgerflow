package com.ledgerflow.model;

import com.ledgerflow.enums.TipoConta;

public class ContaCorrente extends ContaBancaria {

    public ContaCorrente(String cpfTitular, String numero, String agencia, double saldoInicial){
        super(cpfTitular, numero, agencia, saldoInicial, TipoConta.CORRENTE);
    }

    private double limiteChequeEspecial;

    @Override
    public boolean sacar(double valor){
        double saldoAtual = getSaldo();
        if(valor <= 0){
            return false;
        }
        else if(valor > (saldoAtual + limiteChequeEspecial)){
            return false;
        }
        else{
            return super.sacar(valor); // aqui o saldo da com.ledgerflow.model.ContaBancaria é atualizado
        }
    }

}
