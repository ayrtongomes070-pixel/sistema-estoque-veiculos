package com.estoqueveiculos.model;

import com.estoqueveiculos.exception.DadosInvalidosException;

import java.util.Locale;

public class Caminhonete extends Veiculo {

    private double capacidadeCargaKg;

    public Caminhonete(String chassi, String modelo, String marca, int anoFabricacao,
                        double preco, double capacidadeCargaKg) {
        super(chassi, modelo, marca, anoFabricacao, preco);
        setCapacidadeCargaKg(capacidadeCargaKg);
    }

    @Override
    public double calcularDesconto() {
        // Regra de exemplo: 3% de desconto para caminhonetes
        return getPreco() * 0.03;
    }

    @Override
    public String getTipo() {
        return "Caminhonete";
    }

    @Override
    public String getDescricaoDetalhe() {
        return String.format(Locale.forLanguageTag("pt-BR"), "%.0f kg", capacidadeCargaKg);
    }

    public double getCapacidadeCargaKg() {
        return capacidadeCargaKg;
    }

    public void setCapacidadeCargaKg(double capacidadeCargaKg) {
        if (!Double.isFinite(capacidadeCargaKg) || capacidadeCargaKg <= 0) {
            throw new DadosInvalidosException("Capacidade de carga deve ser um valor válido e maior que zero.");
        }
        this.capacidadeCargaKg = capacidadeCargaKg;
    }

    @Override
    public String toString() {
        return super.toString() + " - Carga: " + capacidadeCargaKg + "kg";
    }
}
