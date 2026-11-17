package com.estoqueveiculos.factory;

import com.estoqueveiculos.model.Carro;
import com.estoqueveiculos.model.Veiculo;
import com.estoqueveiculos.util.ConversorNumero;

/**
 * Criador concreto do padrão Factory Method: produz {@link Carro}.
 * O detalhe específico é o número de portas (inteiro).
 */
public class CarroFactory extends VeiculoFactory {

    /** Cria a fábrica de carros. Use preferencialmente {@link VeiculoFactory#paraTipo(String)}. */
    public CarroFactory() {
    }

    /** {@inheritDoc} */
    @Override
    protected Veiculo instanciar(String chassi, String modelo, String marca,
                                 int anoFabricacao, double preco, String detalhe) {
        int portas = ConversorNumero.paraInteiro(detalhe, getRotuloDetalhe());
        return new Carro(chassi, modelo, marca, anoFabricacao, preco, portas);
    }

    /** {@inheritDoc} */
    @Override
    public String getTipo() {
        return "Carro";
    }

    /** {@inheritDoc} */
    @Override
    public String getRotuloDetalhe() {
        return "Número de portas";
    }

    /** {@inheritDoc} */
    @Override
    public String detalheComoTexto(Veiculo veiculo) {
        return String.valueOf(exigirTipo(veiculo, Carro.class).getNumeroPortas());
    }
}
