package com.estoqueveiculos.exception;

/** Indica falha ao carregar ou salvar dados do sistema. */
public class ErroPersistenciaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ErroPersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
