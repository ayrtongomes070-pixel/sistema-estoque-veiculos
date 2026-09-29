package com.estoqueveiculos.repository;

import com.estoqueveiculos.exception.VeiculoNaoEncontradoException;
import com.estoqueveiculos.model.Veiculo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Repositório temporário para executar a aplicação sem configurar banco de dados. */
public class VeiculoRepositoryMemoria implements IVeiculoRepository {
    private final Map<String, Veiculo> veiculos = new LinkedHashMap<>();

    @Override public void salvar(Veiculo veiculo) {
        if (veiculos.containsKey(veiculo.getChassi())) {
            throw new IllegalArgumentException("Já existe um veículo com esse chassi.");
        }
        veiculos.put(veiculo.getChassi(), veiculo);
    }

    @Override public Veiculo buscarPorChassi(String chassi) throws VeiculoNaoEncontradoException {
        Veiculo veiculo = veiculos.get(chassi);
        if (veiculo == null) throw new VeiculoNaoEncontradoException(chassi);
        return veiculo;
    }

    @Override public List<Veiculo> listarTodos() { return new ArrayList<>(veiculos.values()); }

    @Override public void atualizar(Veiculo veiculo) throws VeiculoNaoEncontradoException {
        if (!veiculos.containsKey(veiculo.getChassi())) throw new VeiculoNaoEncontradoException(veiculo.getChassi());
        veiculos.put(veiculo.getChassi(), veiculo);
    }

    @Override public void remover(String chassi) throws VeiculoNaoEncontradoException {
        if (veiculos.remove(chassi) == null) throw new VeiculoNaoEncontradoException(chassi);
    }
}
