package com.estoqueveiculos.model;

/**
 * Representa o vendedor responsável por registrar vendas.
 * Imutável após a criação (sem setters), conforme modelagem da Etapa 2.
 * A senha não é exposta publicamente — fica encapsulada dentro da classe.
 */
public class Vendedor {

    private final int id;
    private final String nome;
    private final String login;
    private final String senha;

    public Vendedor(int id, String nome, String login, String senha) {
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("Login não pode ser vazio.");
        }
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public String toString() {
        return nome + " (login: " + login + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vendedor)) return false;
        return login.equals(((Vendedor) o).login);
    }

    @Override
    public int hashCode() {
        return login.hashCode();
    }
}
