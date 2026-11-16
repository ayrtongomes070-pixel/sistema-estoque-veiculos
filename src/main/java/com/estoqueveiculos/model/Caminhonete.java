package com.estoqueveiculos.model;

import com.estoqueveiculos.exception.DadosInvalidosException;

import java.util.Locale;

/** Representa uma caminhonete e sua capacidade de carga em quilogramas. */
public class Caminhonete extends Veiculo {

    private double capacidadeCargaKg;

    /** Cria uma caminhonete com os dados comuns e a capacidade de carga. */
    public Caminhonete(String chassi, String modelo, String marca, int anoFabricacao,
                        double preco, double capacidadeCargaKg) {
        super(chassi, modelo, marca, anoFabricacao, preco);
        setCapacidadeCargaKg(capacidadeCargaKg);
    }

    /** Calcula o desconto de 3% definido para caminhonetes. */
    @Override
    public double calcularDesconto() {
        // Regra de exemplo: 3% de desconto para caminhonetes
        return getPreco() * 0.03;
    }

    /** Retorna o nome do tipo de veículo. */
    @Override
    public String getTipo() {
        return "Caminhonete";
    }

    /** Retorna a capacidade formatada como detalhe para a interface. */
    @Override
    public String getDescricaoDetalhe() {
        return String.format(Locale.forLanguageTag("pt-BR"), "%.0f kg", capacidadeCargaKg);
    }

    /** Retorna a capacidade de carga em quilogramas. */
    public double getCapacidadeCargaKg() {
        return capacidadeCargaKg;
    }

    /** Altera a capacidade de carga, que deve ser finita e positiva. */
    public void setCapacidadeCargaKg(double capacidadeCargaKg) {
        if (!Double.isFinite(capacidadeCargaKg) || capacidadeCargaKg <= 0) {
            throw new DadosInvalidosException("Capacidade de carga deve ser um valor válido e maior que zero.");
        }
        this.capacidadeCargaKg = capacidadeCargaKg;
    }

    /** Retorna os dados da caminhonete em formato textual. */
    @Override
    public String toString() {
        return super.toString() + " - Carga: " + capacidadeCargaKg + "kg";
    }
}
