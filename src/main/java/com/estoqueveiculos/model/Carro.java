package com.estoqueveiculos.model;

import com.estoqueveiculos.exception.DadosInvalidosException;

public class Carro extends Veiculo {

    private int numeroPortas;

    public Carro(String chassi, String modelo, String marca, int anoFabricacao,
                 double preco, int numeroPortas) {
        super(chassi, modelo, marca, anoFabricacao, preco);
        setNumeroPortas(numeroPortas);
    }

    @Override
    public double calcularDesconto() {
        // Regra de exemplo: 5% de desconto para carros
        return getPreco() * 0.05;
    }

    @Override
    public String getTipo() {
        return "Carro";
    }

    @Override
    public String getDescricaoDetalhe() {
        return numeroPortas + " portas";
    }

    public int getNumeroPortas() {
        return numeroPortas;
    }

    public void setNumeroPortas(int numeroPortas) {
        if (numeroPortas <= 0) {
            throw new DadosInvalidosException("Número de portas deve ser maior que zero.");
        }
        this.numeroPortas = numeroPortas;
    }

    @Override
    public String toString() {
        return super.toString() + " - " + numeroPortas + " portas";
    }
}
