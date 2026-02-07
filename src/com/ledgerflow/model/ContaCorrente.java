package com.ledgerflow.model;

import com.ledgerflow.enums.TipoConta;
import com.ledgerflow.exceptions.InsufficientFundsException;

public class ContaCorrente extends ContaBancaria {

    public ContaCorrente(String cpfTitular, String numero, String agencia, double saldoInicial){
        super(cpfTitular, numero, agencia, saldoInicial, TipoConta.CORRENTE);
    }

    private double limiteChequeEspecial;

    @Override
    public void sacar(double valor){
        if(valor > (getSaldo() + this.limiteChequeEspecial)){
            throw new InsufficientFundsException("Saldo e Limites excedidos!");
        }
        super.sacar(valor);
    }
}
