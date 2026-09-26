package com.estoqueveiculos.controller;

import com.estoqueveiculos.exception.VeiculoNaoEncontradoException;
import com.estoqueveiculos.model.Veiculo;
import com.estoqueveiculos.service.EstoqueService;

import java.util.List;

/**
 * Controller que liga a tela de estoque (estoque.fxml, quando existir) ao EstoqueService.
 * Os métodos aqui não dependem de JavaFX — podem ser chamados tanto por uma
 * tela FXML (via @FXML) quanto por um menu de console, se necessário.
 */
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    public void cadastrarVeiculo(Veiculo veiculo) {
        estoqueService.cadastrarVeiculo(veiculo);
    }

    public List<Veiculo> listarVeiculos() {
        return estoqueService.listarVeiculos();
    }

    public Veiculo buscarVeiculo(String chassi) throws VeiculoNaoEncontradoException {
        return estoqueService.buscarPorChassi(chassi);
    }

    public void atualizarVeiculo(Veiculo veiculo) throws VeiculoNaoEncontradoException {
        estoqueService.atualizarVeiculo(veiculo);
    }

    public void removerVeiculo(String chassi) throws VeiculoNaoEncontradoException {
        estoqueService.removerVeiculo(chassi);
    }

    public double valorTotalEstoque() {
        return estoqueService.valorTotalEstoque();
    }
}
