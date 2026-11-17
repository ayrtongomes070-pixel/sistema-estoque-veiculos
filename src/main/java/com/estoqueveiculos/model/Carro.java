package com.estoqueveiculos.model;

import com.estoqueveiculos.exception.DadosInvalidosException;

/** Representa um carro de passeio e sua quantidade de portas. */
public class Carro extends Veiculo {

    private int numeroPortas;

    /**
     * Cria um carro com os dados comuns e a quantidade de portas.
     * @param chassi chassi que identifica o veículo
     * @param modelo modelo do veículo
     * @param marca marca do veículo
     * @param anoFabricacao ano de fabricação
     * @param preco preço em reais (não negativo)
     * @param numeroPortas quantidade de portas (maior que zero)
     */
    public Carro(String chassi, String modelo, String marca, int anoFabricacao,
                 double preco, int numeroPortas) {
        super(chassi, modelo, marca, anoFabricacao, preco);
        setNumeroPortas(numeroPortas);
    }

    /**
     * Calcula o desconto de 5% definido para carros.
     * @return valor do desconto em reais
     */
    @Override
    public double calcularDesconto() {
        // Regra de exemplo: 5% de desconto para carros
        return getPreco() * 0.05;
    }

    /**
     * Retorna o nome do tipo de veículo.
     * @return nome do tipo
     */
    @Override
    public String getTipo() {
        return "Carro";
    }

    /**
     * Retorna a descrição da quantidade de portas.
     * @return descrição do detalhe
     */
    @Override
    public String getDescricaoDetalhe() {
        return numeroPortas + " portas";
    }

    /**
     * Retorna a quantidade de portas.
     * @return a quantidade de portas
     */
    public int getNumeroPortas() {
        return numeroPortas;
    }

    /**
     * Altera a quantidade de portas; o valor deve ser positivo.
     * @param numeroPortas quantidade de portas (maior que zero)
     */
    public void setNumeroPortas(int numeroPortas) {
        if (numeroPortas <= 0) {
            throw new DadosInvalidosException("Número de portas deve ser maior que zero.");
        }
        this.numeroPortas = numeroPortas;
    }

    /**
     * Retorna os dados do carro em formato textual.
     * @return os dados do carro em formato textual
     */
    @Override
    public String toString() {
        return super.toString() + " - " + numeroPortas + " portas";
    }
}
