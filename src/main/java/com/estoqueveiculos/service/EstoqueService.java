package com.estoqueveiculos.service;

import com.estoqueveiculos.exception.DadosInvalidosException;
import com.estoqueveiculos.exception.VeiculoNaoEncontradoException;
import com.estoqueveiculos.model.Veiculo;
import com.estoqueveiculos.repository.IVeiculoRepository;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Regras de negócio relacionadas ao estoque de veículos.
 */
public class EstoqueService {

    private final IVeiculoRepository veiculoRepository;

    public EstoqueService(IVeiculoRepository veiculoRepository) {
        if (veiculoRepository == null) throw new DadosInvalidosException("Repositório de veículos é obrigatório.");
        this.veiculoRepository = veiculoRepository;
    }

    public void cadastrarVeiculo(Veiculo veiculo) {
        veiculoRepository.salvar(veiculo);
    }

    public void atualizarVeiculo(Veiculo veiculo) throws VeiculoNaoEncontradoException {
        veiculoRepository.atualizar(veiculo);
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

    public double valorTotalEstoque() {
        return listarVeiculos().stream().mapToDouble(Veiculo::getPreco).sum();
    }

    /**
     * Pesquisa por marca, modelo ou chassi, sem diferenciar maiúsculas e minúsculas.
     * Termo vazio ou nulo devolve todos os veículos.
     */
    public List<Veiculo> pesquisar(String termo) {
        String busca = termo == null ? "" : termo.trim().toLowerCase(Locale.ROOT);
        return listarVeiculos().stream()
                .filter(v -> (v.getMarca() + " " + v.getModelo() + " " + v.getChassi())
                        .toLowerCase(Locale.ROOT).contains(busca))
                .collect(Collectors.toList());
    }

    /**
     * Marcas presentes no estoque. {@link Set} garante que cada marca aparece uma única vez
     * e {@link TreeSet} mantém a ordem alfabética.
     */
    public Set<String> listarMarcas() {
        return listarVeiculos().stream()
                .map(Veiculo::getMarca)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /** Quantidade de veículos por tipo (ex.: Carro=2, Caminhonete=1), em ordem alfabética. */
    public Map<String, Long> quantidadePorTipo() {
        return listarVeiculos().stream()
                .collect(Collectors.groupingBy(Veiculo::getTipo, TreeMap::new, Collectors.counting()));
    }
}
