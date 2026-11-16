package com.estoqueveiculos.model;

import com.estoqueveiculos.exception.DadosInvalidosException;

/** Representa um carro de passeio e sua quantidade de portas. */
public class Carro extends Veiculo {

    private int numeroPortas;

    /** Cria um carro com os dados comuns e a quantidade de portas. */
    public Carro(String chassi, String modelo, String marca, int anoFabricacao,
                 double preco, int numeroPortas) {
        super(chassi, modelo, marca, anoFabricacao, preco);
        setNumeroPortas(numeroPortas);
    }

    /** Calcula o desconto de 5% definido para carros. */
    @Override
    public double calcularDesconto() {
        // Regra de exemplo: 5% de desconto para carros
        return getPreco() * 0.05;
    }

    /** Retorna o nome do tipo de veículo. */
    @Override
    public String getTipo() {
        return "Carro";
    }

    /** Retorna a descrição da quantidade de portas. */
    @Override
    public String getDescricaoDetalhe() {
        return numeroPortas + " portas";
    }

    /** Retorna a quantidade de portas. */
    public int getNumeroPortas() {
        return numeroPortas;
    }

    /** Altera a quantidade de portas; o valor deve ser positivo. */
    public void setNumeroPortas(int numeroPortas) {
        if (numeroPortas <= 0) {
            throw new DadosInvalidosException("Número de portas deve ser maior que zero.");
        }
        this.numeroPortas = numeroPortas;
    }

    /** Retorna os dados do carro em formato textual. */
    @Override
    public String toString() {
        return super.toString() + " - " + numeroPortas + " portas";
    }
}
