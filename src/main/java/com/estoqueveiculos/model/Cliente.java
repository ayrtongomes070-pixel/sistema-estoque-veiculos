package com.estoqueveiculos.model;

import com.estoqueveiculos.exception.DadosInvalidosException;

/**
 * Representa o cliente comprador de um veículo.
 * Imutável após a criação (sem setters), conforme modelagem da Etapa 2.
 */
public class Cliente {

    private final String cpf;
    private final String nome;
    private final String telefone;
    private final String email;

    /** Cria um cliente; CPF e nome são obrigatórios. */
    public Cliente(String cpf, String nome, String telefone, String email) {
        if (cpf == null || cpf.isBlank()) {
            throw new DadosInvalidosException("CPF não pode ser vazio.");
        }
        if (nome == null || nome.isBlank()) {
            throw new DadosInvalidosException("Nome não pode ser vazio.");
        }
        this.cpf = cpf;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }

    /** Retorna o CPF do cliente. */
    public String getCpf() {
        return cpf;
    }

    /** Retorna o nome do cliente. */
    public String getNome() {
        return nome;
    }

    /** Retorna o telefone do cliente. */
    public String getTelefone() {
        return telefone;
    }

    /** Retorna o e-mail do cliente. */
    public String getEmail() {
        return email;
    }

    /** Retorna o nome e o CPF do cliente em formato textual. */
    @Override
    public String toString() {
        return nome + " (CPF: " + cpf + ")";
    }

    /** Compara clientes pelo CPF. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente)) return false;
        return cpf.equals(((Cliente) o).cpf);
    }

    /** Calcula o código hash usando o CPF. */
    @Override
    public int hashCode() {
        return cpf.hashCode();
    }
}
