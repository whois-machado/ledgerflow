package com.ledgerflow.model;

public class Cliente {
    private final String nome;
    private final String cpf;
    private String email;
    private String telefone;

    // cadastro inicial do cliente no banco
    public Cliente(String nome, String cpf, String email, String telefone){
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
    }

    public String getNome(){
        return nome;
    }

    public String getCpf(){
        return cpf;
    }

    public String getEmail(){
        return email;
    }

    public String getTelefone(){
        return telefone;
    }
}
