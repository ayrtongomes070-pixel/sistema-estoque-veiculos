package com.estoqueveiculos.exception;

/** Indica a tentativa de cadastrar um veículo com um chassi que já existe no estoque. */
public class VeiculoDuplicadoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public VeiculoDuplicadoException(String chassi) {
        super("Já existe um veículo cadastrado com o chassi '" + chassi + "'.");
    }
}
