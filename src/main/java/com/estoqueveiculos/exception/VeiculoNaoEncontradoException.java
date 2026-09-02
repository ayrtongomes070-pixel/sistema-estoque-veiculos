package com.estoqueveiculos.exception;

public class VeiculoNaoEncontradoException extends Exception {

    public VeiculoNaoEncontradoException(String chassi) {
        super("Veículo com chassi '" + chassi + "' não encontrado.");
    }
}
