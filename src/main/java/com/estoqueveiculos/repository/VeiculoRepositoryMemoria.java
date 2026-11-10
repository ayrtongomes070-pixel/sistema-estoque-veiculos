package com.estoqueveiculos.repository;

import com.estoqueveiculos.exception.DadosInvalidosException;
import com.estoqueveiculos.exception.VeiculoDuplicadoException;
import com.estoqueveiculos.exception.VeiculoNaoEncontradoException;
import com.estoqueveiculos.model.Veiculo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Repositório de veículos mantido em memória.
 * Usa um {@link Map} indexado pelo chassi: a busca é direta (sem percorrer a lista)
 * e não permite dois veículos com o mesmo chassi. {@link LinkedHashMap} preserva a ordem de cadastro.
 * Também serve de base para {@link VeiculoRepositoryArquivo}, que acrescenta a gravação em disco.
 */
public class VeiculoRepositoryMemoria implements IVeiculoRepository {
    private final Map<String, Veiculo> veiculos = new LinkedHashMap<>();

    @Override
    public void salvar(Veiculo veiculo) {
        validar(veiculo);
        if (veiculos.containsKey(veiculo.getChassi())) throw new VeiculoDuplicadoException(veiculo.getChassi());
        veiculos.put(veiculo.getChassi(), veiculo);
    }

    @Override
    public Veiculo buscarPorChassi(String chassi) throws VeiculoNaoEncontradoException {
        Veiculo veiculo = veiculos.get(chassi);
        if (veiculo == null) throw new VeiculoNaoEncontradoException(chassi);
        return veiculo;
    }

    @Override
    public List<Veiculo> listarTodos() {
        return new ArrayList<>(veiculos.values());
    }

    @Override
    public void atualizar(Veiculo veiculo) throws VeiculoNaoEncontradoException {
        validar(veiculo);
        if (!veiculos.containsKey(veiculo.getChassi())) throw new VeiculoNaoEncontradoException(veiculo.getChassi());
        veiculos.put(veiculo.getChassi(), veiculo);
    }

    @Override
    public void remover(String chassi) throws VeiculoNaoEncontradoException {
        if (veiculos.remove(chassi) == null) throw new VeiculoNaoEncontradoException(chassi);
    }

    /** Cópia do estado atual, usada pelas subclasses para desfazer uma operação que falhou. */
    protected Map<String, Veiculo> copiarDados() {
        return new LinkedHashMap<>(veiculos);
    }

    /** Restaura um estado copiado anteriormente com {@link #copiarDados()}. */
    protected void restaurarDados(Map<String, Veiculo> estadoAnterior) {
        veiculos.clear();
        veiculos.putAll(estadoAnterior);
    }

    private static void validar(Veiculo veiculo) {
        if (veiculo == null) throw new DadosInvalidosException("Veículo não pode ser nulo.");
    }
}
