package com.estoqueveiculos.repository;

import com.estoqueveiculos.model.Veiculo;

import java.util.List;

/**
 * Implementação de IVeiculoRepository usando JDBC + MySQL — PLANEJADA, ainda não implementada.
 * Enquanto isso, cada método lança {@link UnsupportedOperationException} com uma mensagem clara,
 * em vez de não fazer nada e perder dados silenciosamente.
 * A aplicação usa {@link VeiculoRepositoryArquivo}.
 */
public class VeiculoRepositoryJDBC implements IVeiculoRepository {

    private static final String MENSAGEM =
            "Persistência em MySQL ainda não implementada. Use VeiculoRepositoryArquivo.";

    @Override
    public void salvar(Veiculo veiculo) {
        throw new UnsupportedOperationException(MENSAGEM);
    }

    @Override
    public Veiculo buscarPorChassi(String chassi) {
        throw new UnsupportedOperationException(MENSAGEM);
    }

    @Override
    public List<Veiculo> listarTodos() {
        throw new UnsupportedOperationException(MENSAGEM);
    }

    @Override
    public void atualizar(Veiculo veiculo) {
        throw new UnsupportedOperationException(MENSAGEM);
    }

    @Override
    public void remover(String chassi) {
        throw new UnsupportedOperationException(MENSAGEM);
    }
}
