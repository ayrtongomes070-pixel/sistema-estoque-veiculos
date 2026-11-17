package com.estoqueveiculos.repository;

import com.estoqueveiculos.exception.VeiculoNaoEncontradoException;
import com.estoqueveiculos.model.Veiculo;

import java.util.List;

/**
 * Estratégia GoF para persistência de veículos.
 * Os serviços dependem deste contrato e podem receber uma implementação em arquivo,
 * em memória ou, futuramente, em banco de dados.
 */
public interface IVeiculoRepository {

    /** Persiste um novo veículo.
     * @param veiculo veículo que será armazenado
     * @throws IllegalArgumentException se o veículo for inválido ou já estiver cadastrado
     */
    void salvar(Veiculo veiculo);

    /** Busca um veículo pelo chassi.
     * @param chassi identificador do veículo
     * @return veículo encontrado
     * @throws VeiculoNaoEncontradoException se não houver veículo com o chassi informado
     */
    Veiculo buscarPorChassi(String chassi) throws VeiculoNaoEncontradoException;

    /** Retorna uma cópia da lista de veículos armazenados.
     * @return veículos na ordem de cadastro
     */
    List<Veiculo> listarTodos();

    /** Atualiza um veículo já cadastrado.
     * @param veiculo veículo contendo os dados atualizados
     * @throws VeiculoNaoEncontradoException se o chassi não estiver cadastrado
     */
    void atualizar(Veiculo veiculo) throws VeiculoNaoEncontradoException;

    /** Remove o veículo identificado pelo chassi.
     * @param chassi identificador do veículo
     * @throws VeiculoNaoEncontradoException se o chassi não estiver cadastrado
     */
    void remover(String chassi) throws VeiculoNaoEncontradoException;
}
