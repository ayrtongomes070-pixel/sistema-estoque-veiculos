package com.estoqueveiculos.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa o estoque de veículos de uma loja (versão em memória).
 * Observação: para persistência real, ver repository.IVeiculoRepository.
 */
public class Estoque {

    private List<Veiculo> veiculos;

    public Estoque() {
        this.veiculos = new ArrayList<>();
    }

    public void adicionar(Veiculo veiculo) {
        if (veiculo == null) {
            throw new IllegalArgumentException("Veículo não pode ser nulo.");
        }
        veiculos.add(veiculo);
    }

    public void remover(Veiculo veiculo) {
        veiculos.remove(veiculo);
    }

    public List<Veiculo> getVeiculos() {
        return Collections.unmodifiableList(new ArrayList<>(veiculos));
    }

    public int quantidade() {
        return veiculos.size();
    }
}
