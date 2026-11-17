package com.estoqueveiculos.model;

import com.estoqueveiculos.exception.DadosInvalidosException;

import java.util.Locale;

/** Representa uma caminhonete e sua capacidade de carga em quilogramas. */
public class Caminhonete extends Veiculo {

    private double capacidadeCargaKg;

    /**
     * Cria uma caminhonete com os dados comuns e a capacidade de carga.
     * @param chassi chassi que identifica o veículo
     * @param modelo modelo do veículo
     * @param marca marca do veículo
     * @param anoFabricacao ano de fabricação
     * @param preco preço em reais (não negativo)
     * @param capacidadeCargaKg capacidade de carga em kg (maior que zero)
     */
    public Caminhonete(String chassi, String modelo, String marca, int anoFabricacao,
                        double preco, double capacidadeCargaKg) {
        super(chassi, modelo, marca, anoFabricacao, preco);
        setCapacidadeCargaKg(capacidadeCargaKg);
    }

    /**
     * Calcula o desconto de 3% definido para caminhonetes.
     * @return valor do desconto em reais
     */
    @Override
    public double calcularDesconto() {
        // Regra de exemplo: 3% de desconto para caminhonetes
        return getPreco() * 0.03;
    }

    /**
     * Retorna o nome do tipo de veículo.
     * @return nome do tipo
     */
    @Override
    public String getTipo() {
        return "Caminhonete";
    }

    /**
     * Retorna a capacidade formatada como detalhe para a interface.
     * @return descrição do detalhe
     */
    @Override
    public String getDescricaoDetalhe() {
        return String.format(Locale.forLanguageTag("pt-BR"), "%.0f kg", capacidadeCargaKg);
    }

    /**
     * Retorna a capacidade de carga em quilogramas.
     * @return a capacidade de carga em quilogramas
     */
    public double getCapacidadeCargaKg() {
        return capacidadeCargaKg;
    }

    /**
     * Altera a capacidade de carga, que deve ser finita e positiva.
     * @param capacidadeCargaKg capacidade de carga em kg (maior que zero)
     */
    public void setCapacidadeCargaKg(double capacidadeCargaKg) {
        if (!Double.isFinite(capacidadeCargaKg) || capacidadeCargaKg <= 0) {
            throw new DadosInvalidosException("Capacidade de carga deve ser um valor válido e maior que zero.");
        }
        this.capacidadeCargaKg = capacidadeCargaKg;
    }

    /**
     * Retorna os dados da caminhonete em formato textual.
     * @return os dados da caminhonete em formato textual
     */
    @Override
    public String toString() {
        return super.toString() + " - Carga: " + capacidadeCargaKg + "kg";
    }
}
