package com.estoqueveiculos.factory;

import com.estoqueveiculos.model.Caminhonete;
import com.estoqueveiculos.model.Veiculo;
import com.estoqueveiculos.util.ConversorNumero;

/**
 * Criador concreto do padrão Factory Method: produz {@link Caminhonete}.
 * O detalhe específico é a capacidade de carga em kg (decimal).
 */
public class CaminhoneteFactory extends VeiculoFactory {

    /** Cria a fábrica de caminhonetes. Use preferencialmente {@link VeiculoFactory#paraTipo(String)}. */
    public CaminhoneteFactory() {
    }

    /** {@inheritDoc} */
    @Override
    protected Veiculo instanciar(String chassi, String modelo, String marca,
                                 int anoFabricacao, double preco, String detalhe) {
        double capacidade = ConversorNumero.paraDecimal(detalhe, getRotuloDetalhe());
        return new Caminhonete(chassi, modelo, marca, anoFabricacao, preco, capacidade);
    }

    /** {@inheritDoc} */
    @Override
    public String getTipo() {
        return "Caminhonete";
    }

    /** {@inheritDoc} */
    @Override
    public String getRotuloDetalhe() {
        return "Capacidade de carga (kg)";
    }

    /** {@inheritDoc} */
    @Override
    public String detalheComoTexto(Veiculo veiculo) {
        return ConversorNumero.paraTexto(exigirTipo(veiculo, Caminhonete.class).getCapacidadeCargaKg());
    }
}
