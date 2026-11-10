package com.estoqueveiculos.controller;

import com.estoqueveiculos.exception.VeiculoNaoEncontradoException;
import com.estoqueveiculos.model.Veiculo;
import com.estoqueveiculos.service.EstoqueService;

import java.util.List;
import java.util.Set;

/**
 * Controller que liga a tela de estoque ao EstoqueService.
 * Os métodos aqui não dependem de JavaFX — podem ser chamados tanto pela
 * interface gráfica quanto por um menu de console, se necessário.
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

    public List<Veiculo> pesquisar(String termo) {
        return estoqueService.pesquisar(termo);
    }

    public Set<String> listarMarcas() {
        return estoqueService.listarMarcas();
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
