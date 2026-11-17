package com.estoqueveiculos.exception;

/** Indica que nenhum veículo com o chassi informado existe no estoque. */
public class VeiculoNaoEncontradoException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Cria a exceção com o chassi que não foi localizado.
     * @param chassi identificador pesquisado
     */
    public VeiculoNaoEncontradoException(String chassi) {
        super("Veículo com chassi '" + chassi + "' não encontrado.");
    }
}
