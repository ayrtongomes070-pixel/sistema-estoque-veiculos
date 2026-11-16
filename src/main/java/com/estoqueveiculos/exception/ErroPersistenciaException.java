package com.estoqueveiculos.exception;

/** Indica falha ao carregar ou salvar dados do sistema. */
public class ErroPersistenciaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /** Cria uma falha de persistência com mensagem para o usuário e causa técnica.
     * @param mensagem descrição da operação que falhou
     * @param causa exceção original lançada pelo sistema de arquivos
     */
    public ErroPersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
