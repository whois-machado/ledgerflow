package com.ledgerflow.model;

import com.ledgerflow.enums.TipoTransacao;

import java.time.LocalDateTime;

public class Transacao {

    private final String id;
    private final String numeroContaOrigem;
    private final String numeroContaDestino;
    private final double valor;
    private final TipoTransacao tipo;
    private final LocalDateTime dataHora;

    public Transacao(String id, String numeroContaOrigem, String numeroContaDestino, double valor, TipoTransacao tipo, LocalDateTime dataHora){
        this.id = id;
        this.numeroContaOrigem = numeroContaOrigem;
        this.numeroContaDestino = numeroContaDestino;
        this.valor = valor;
        this.tipo = tipo;
        this.dataHora = dataHora;
    }

    public String getId(){
        return id;
    }

    public String getNumeroContaOrigem(){
        return numeroContaOrigem;
    }

    public String getNumeroContaDestino(){
        return numeroContaDestino;
    }

    public double getValor(){
        return valor;
    }

    public TipoTransacao getTipo() {
        return tipo;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    // sem setters(imutabilidade de uma transacao)
}
