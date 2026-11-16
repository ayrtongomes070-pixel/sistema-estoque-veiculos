package com.estoqueveiculos.controller;

import com.estoqueveiculos.exception.VeiculoNaoEncontradoException;
import com.estoqueveiculos.model.Veiculo;
import com.estoqueveiculos.service.EstoqueService;
import com.estoqueveiculos.service.EstoqueObserver;

import java.util.List;
import java.util.Set;

/**
 * Controller que liga a tela de estoque ao EstoqueService.
 * Os métodos aqui não dependem de JavaFX — podem ser chamados tanto pela
 * interface gráfica quanto por um menu de console, se necessário.
 */
public class EstoqueController {

    private final EstoqueService estoqueService;

    /** Cria o controller associado ao serviço de estoque informado. */
    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    /** Cadastra um veículo no estoque. */
    public void cadastrarVeiculo(Veiculo veiculo) {
        estoqueService.cadastrarVeiculo(veiculo);
    }

    /** Lista os veículos cadastrados. */
    public List<Veiculo> listarVeiculos() {
        return estoqueService.listarVeiculos();
    }

    /** Pesquisa por marca, modelo ou chassi. */
    public List<Veiculo> pesquisar(String termo) {
        return estoqueService.pesquisar(termo);
    }

    /** Lista as marcas cadastradas sem repetição. */
    public Set<String> listarMarcas() {
        return estoqueService.listarMarcas();
    }

    /** Busca um veículo pelo chassi.
     * @throws VeiculoNaoEncontradoException quando o veículo não existe
     */
    public Veiculo buscarVeiculo(String chassi) throws VeiculoNaoEncontradoException {
        return estoqueService.buscarPorChassi(chassi);
    }

    /** Atualiza um veículo existente.
     * @throws VeiculoNaoEncontradoException quando o veículo não existe
     */
    public void atualizarVeiculo(Veiculo veiculo) throws VeiculoNaoEncontradoException {
        estoqueService.atualizarVeiculo(veiculo);
    }

    /** Remove um veículo pelo chassi.
     * @throws VeiculoNaoEncontradoException quando o veículo não existe
     */
    public void removerVeiculo(String chassi) throws VeiculoNaoEncontradoException {
        estoqueService.removerVeiculo(chassi);
    }

    /** Retorna o valor total dos veículos cadastrados. */
    public double valorTotalEstoque() {
        return estoqueService.valorTotalEstoque();
    }

    /** Registra um observador para receber avisos quando o estoque for alterado. */
    public void adicionarObservador(EstoqueObserver observador) {
        estoqueService.adicionarObservador(observador);
    }

    /** Remove um observador registrado anteriormente. */
    public void removerObservador(EstoqueObserver observador) {
        estoqueService.removerObservador(observador);
    }
}
