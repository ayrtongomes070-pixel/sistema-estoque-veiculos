package com.estoqueveiculos.controller;

import com.estoqueveiculos.service.EstoqueService;

/**
 * Controller que liga a tela de estoque (estoque.fxml) ao EstoqueService.
 * TODO: anotar campos com @FXML conforme os componentes do FXML forem criados.
 */
public class EstoqueController {

    private EstoqueService estoqueService;

    public void setEstoqueService(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    // TODO: métodos de inicialização e ações de botões (cadastrar, remover, listar)
}
