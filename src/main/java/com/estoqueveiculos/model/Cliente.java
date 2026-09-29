package com.estoqueveiculos.model;

/**
 * Representa o cliente comprador de um veículo.
 * Imutável após a criação (sem setters), conforme modelagem da Etapa 2.
 */
public class Cliente {

    private final String cpf;
    private final String nome;
    private final String telefone;
    private final String email;

    public Cliente(String cpf, String nome, String telefone, String email) {
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("CPF não pode ser vazio.");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }
        this.cpf = cpf;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return nome + " (CPF: " + cpf + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente)) return false;
        return cpf.equals(((Cliente) o).cpf);
    }

    @Override
    public int hashCode() {
        return cpf.hashCode();
    }
}
