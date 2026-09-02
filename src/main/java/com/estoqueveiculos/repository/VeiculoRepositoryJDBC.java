package com.estoqueveiculos.repository;

import com.estoqueveiculos.exception.VeiculoNaoEncontradoException;
import com.estoqueveiculos.model.Veiculo;

import java.util.List;

/**
 * Implementação de IVeiculoRepository usando JDBC + MySQL.
 * TODO: implementar os métodos usando java.sql.Connection (ver util.ConexaoBD).
 */
public class VeiculoRepositoryJDBC implements IVeiculoRepository {

    @Override
    public void salvar(Veiculo veiculo) {
        // TODO: implementar INSERT via JDBC
    }

    @Override
    public Veiculo buscarPorChassi(String chassi) throws VeiculoNaoEncontradoException {
        // TODO: implementar SELECT via JDBC
        throw new VeiculoNaoEncontradoException(chassi);
    }

    @Override
    public List<Veiculo> listarTodos() {
        // TODO: implementar SELECT * via JDBC
        return List.of();
    }

    @Override
    public void atualizar(Veiculo veiculo) throws VeiculoNaoEncontradoException {
        // TODO: implementar UPDATE via JDBC
    }

    @Override
    public void remover(String chassi) throws VeiculoNaoEncontradoException {
        // TODO: implementar DELETE via JDBC
    }
}
