package com.estoqueveiculos.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa o estoque de veículos de uma loja.
 */
public class Estoque {

    private List<Veiculo> veiculos;

    public Estoque() {
        this.veiculos = new ArrayList<>();
    }

    public void adicionar(Veiculo veiculo) {
        veiculos.add(veiculo);
    }

    public void remover(Veiculo veiculo) {
        veiculos.remove(veiculo);
    }

    public List<Veiculo> getVeiculos() {
        return veiculos;
    }

    public int quantidade() {
        return veiculos.size();
    }
}
