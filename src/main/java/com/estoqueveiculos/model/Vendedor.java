package com.estoqueveiculos.model;

import com.estoqueveiculos.exception.DadosInvalidosException;

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

    /**
     * Cria um vendedor; o login é obrigatório.
     * @param id identificador numérico
     * @param nome nome completo
     * @param login login de acesso (obrigatório)
     * @param senha senha de acesso
     */
    public Vendedor(int id, String nome, String login, String senha) {
        if (login == null || login.isBlank()) {
            throw new DadosInvalidosException("Login não pode ser vazio.");
        }
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
    }

    /**
     * Retorna o identificador do vendedor.
     * @return o identificador do vendedor
     */
    public int getId() {
        return id;
    }

    /**
     * Retorna o nome do vendedor.
     * @return o nome do vendedor
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna o login; a senha não é exposta pela API pública.
     * @return o login; a senha não é exposta pela API pública
     */
    public String getLogin() {
        return login;
    }

    /**
     * Retorna o nome e o login em formato textual.
     * @return o nome e o login em formato textual
     */
    @Override
    public String toString() {
        return nome + " (login: " + login + ")";
    }

    /**
     * Compara vendedores pelo login.
     * @param o o
     * @return {@code true} se os objetos tiverem a mesma identificação
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vendedor)) return false;
        return login.equals(((Vendedor) o).login);
    }

    /**
     * Calcula o código hash usando o login.
     * @return código hash do objeto
     */
    @Override
    public int hashCode() {
        return login.hashCode();
    }
}
