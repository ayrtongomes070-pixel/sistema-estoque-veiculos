package com.estoqueveiculos.service;

import com.estoqueveiculos.exception.VeiculoNaoEncontradoException;
import com.estoqueveiculos.model.Veiculo;
import com.estoqueveiculos.repository.IVeiculoRepository;

import java.util.List;

/**
 * Regras de negócio relacionadas ao estoque de veículos.
 */
public class EstoqueService {

    private final IVeiculoRepository veiculoRepository;

    public EstoqueService(IVeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    public void cadastrarVeiculo(Veiculo veiculo) {
        veiculoRepository.salvar(veiculo);
    }

    public void atualizarVeiculo(Veiculo veiculo) throws VeiculoNaoEncontradoException {
        veiculoRepository.atualizar(veiculo);
    }

    public double valorTotalEstoque() {
        return listarVeiculos().stream().mapToDouble(Veiculo::getPreco).sum();
    }

    public List<Veiculo> listarVeiculos() {
        return veiculoRepository.listarTodos();
    }

    public Veiculo buscarPorChassi(String chassi) throws VeiculoNaoEncontradoException {
        return veiculoRepository.buscarPorChassi(chassi);
    }

    public void removerVeiculo(String chassi) throws VeiculoNaoEncontradoException {
        veiculoRepository.remover(chassi);
    }
}
