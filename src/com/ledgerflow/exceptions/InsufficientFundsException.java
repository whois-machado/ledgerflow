package com.ledgerflow.exceptions;

public class InsufficientFundsException extends RuntimeException{

    public InsufficientFundsException(String massage){
        super(massage);
    }
}
