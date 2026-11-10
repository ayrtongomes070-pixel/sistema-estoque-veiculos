package com.estoqueveiculos.exception;

/**
 * Indica que um dado informado pelo usuário (ou lido do arquivo) é inválido.
 * Estende IllegalArgumentException para continuar compatível com quem já trata esse tipo.
 */
public class DadosInvalidosException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;

    public DadosInvalidosException(String mensagem) {
        super(mensagem);
    }
}
