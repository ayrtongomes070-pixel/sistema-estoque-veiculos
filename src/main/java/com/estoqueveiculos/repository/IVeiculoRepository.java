package com.estoqueveiculos.repository;

import com.estoqueveiculos.exception.VeiculoNaoEncontradoException;
import com.estoqueveiculos.model.Veiculo;

import java.util.List;

/**
 * Contrato para acesso a dados de veículos.
 * Permite trocar a implementação (JDBC, memória, etc.) sem afetar o resto do sistema.
 */
public interface IVeiculoRepository {

    void salvar(Veiculo veiculo);

    Veiculo buscarPorChassi(String chassi) throws VeiculoNaoEncontradoException;

    List<Veiculo> listarTodos();

    void atualizar(Veiculo veiculo) throws VeiculoNaoEncontradoException;

    void remover(String chassi) throws VeiculoNaoEncontradoException;
}
