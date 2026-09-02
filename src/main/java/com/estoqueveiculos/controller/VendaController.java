package com.estoqueveiculos.controller;

import com.estoqueveiculos.service.VendaService;

/**
 * Controller que liga a tela de venda (venda.fxml) ao VendaService.
 * TODO: anotar campos com @FXML conforme os componentes do FXML forem criados.
 */
public class VendaController {

    private VendaService vendaService;

    public void setVendaService(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    // TODO: métodos de inicialização e ações de botões (registrar venda, listar vendas)
}
