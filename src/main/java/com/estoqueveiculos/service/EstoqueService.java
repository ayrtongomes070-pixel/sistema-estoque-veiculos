package com.estoqueveiculos.service;

import com.estoqueveiculos.exception.DadosInvalidosException;
import com.estoqueveiculos.exception.VeiculoNaoEncontradoException;
import com.estoqueveiculos.model.Veiculo;
import com.estoqueveiculos.repository.IVeiculoRepository;

import java.util.ArrayList;
import java.util.Locale;
import java.util.List;
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
    private final List<EstoqueObserver> observadores = new ArrayList<>();

    /**
     * Cria o serviço com a estratégia de repositório fornecida.
     * @param veiculoRepository estratégia de persistência de veículos
     */
    public EstoqueService(IVeiculoRepository veiculoRepository) {
        if (veiculoRepository == null) throw new DadosInvalidosException("Repositório de veículos é obrigatório.");
        this.veiculoRepository = veiculoRepository;
    }

    /**
     * Cadastra um veículo e notifica os observadores após o salvamento.
     * @param veiculo veículo a cadastrar
     */
    public void cadastrarVeiculo(Veiculo veiculo) {
        veiculoRepository.salvar(veiculo);
        notificarAlteracao();
    }

    /**
     * Atualiza um veículo e notifica os observadores após a alteração.
     * @param veiculo veículo com os dados atualizados
     * @throws VeiculoNaoEncontradoException se o chassi não estiver cadastrado
     */
    public void atualizarVeiculo(Veiculo veiculo) throws VeiculoNaoEncontradoException {
        veiculoRepository.atualizar(veiculo);
        notificarAlteracao();
    }

    /**
     * Retorna os veículos cadastrados.
     * @return lista de veículos cadastrados
     */
    public List<Veiculo> listarVeiculos() {
        return veiculoRepository.listarTodos();
    }

    /**
     * Busca um veículo pelo chassi.
     * @param chassi chassi que identifica o veículo
     * @return veículo encontrado
     * @throws VeiculoNaoEncontradoException se o chassi não estiver cadastrado
     */
    public Veiculo buscarPorChassi(String chassi) throws VeiculoNaoEncontradoException {
        return veiculoRepository.buscarPorChassi(chassi);
    }

    /**
     * Remove um veículo e notifica os observadores após a remoção.
     * @param chassi chassi que identifica o veículo
     * @throws VeiculoNaoEncontradoException se o chassi não estiver cadastrado
     */
    public void removerVeiculo(String chassi) throws VeiculoNaoEncontradoException {
        veiculoRepository.remover(chassi);
        notificarAlteracao();
    }

    /**
     * Retorna a soma dos preços dos veículos disponíveis no estoque.
     * @return soma dos preços em reais
     */
    public double valorTotalEstoque() {
        return listarVeiculos().stream().mapToDouble(Veiculo::getPreco).sum();
    }

    /**
     * Pesquisa marca, modelo ou chassi, sem diferenciar maiúsculas e minúsculas.
     * @param termo texto pesquisado; vazio ou nulo retorna todos
     * @return veículos que contêm o termo
     */
    public List<Veiculo> pesquisar(String termo) {
        String busca = termo == null ? "" : termo.trim().toLowerCase(Locale.ROOT);
        return listarVeiculos().stream()
                .filter(v -> (v.getMarca() + " " + v.getModelo() + " " + v.getChassi())
                        .toLowerCase(Locale.ROOT).contains(busca))
                .collect(Collectors.toList());
    }

    /**
     * Retorna as marcas distintas em ordem alfabética.
     * @return conjunto ordenado de marcas sem repetição
     */
    public Set<String> listarMarcas() {
        return listarVeiculos().stream()
                .map(Veiculo::getMarca)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Retorna a quantidade de veículos agrupada por tipo e ordenada pelo nome do tipo.
     * @return mapa tipo → quantidade
     */
    public Map<String, Long> quantidadePorTipo() {
        return listarVeiculos().stream()
                .collect(Collectors.groupingBy(Veiculo::getTipo, TreeMap::new, Collectors.counting()));
    }

    /**
     * Registra um observador que será notificado após alterações bem-sucedidas no estoque.
     * @param observador observador do estoque
     */
    public void adicionarObservador(EstoqueObserver observador) {
        if (observador == null) throw new DadosInvalidosException("Observador não pode ser nulo.");
        if (!observadores.contains(observador)) observadores.add(observador);
    }

    /**
     * Remove um observador previamente registrado.
     * @param observador observador do estoque
     */
    public void removerObservador(EstoqueObserver observador) {
        observadores.remove(observador);
    }

    private void notificarAlteracao() {
        List.copyOf(observadores).forEach(EstoqueObserver::aoAlterarEstoque);
    }
}
