package com.estoqueveiculos.model;

/**
 * Classe abstrata que representa um veículo genérico.
 * Não deve ser instanciada diretamente — use as subclasses (Carro, Caminhonete).
 */
public abstract class Veiculo {

    private String chassi;
    private String modelo;
    private String marca;
    private int anoFabricacao;
    private double preco;

    public Veiculo(String chassi, String modelo, String marca, int anoFabricacao, double preco) {
        if (chassi == null || chassi.isBlank()) {
            throw new IllegalArgumentException("Chassi não pode ser vazio.");
        }
        if (preco < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo.");
        }
        this.chassi = chassi;
        this.modelo = modelo;
        this.marca = marca;
        this.anoFabricacao = anoFabricacao;
        this.preco = preco;
    }

    // Método abstrato — cada tipo de veículo calcula seu próprio desconto/imposto (polimorfismo)
    public abstract double calcularDesconto();

    public String getChassi() {
        return chassi;
    }

    public void setChassi(String chassi) {
        this.chassi = chassi;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(int anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        if (preco < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo.");
        }
        this.preco = preco;
    }

    @Override
    public String toString() {
        return String.format("%s %s (%d) - Chassi: %s - R$ %.2f",
                marca, modelo, anoFabricacao, chassi, preco);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Veiculo)) return false;
        return chassi.equals(((Veiculo) o).chassi);
    }

    @Override
    public int hashCode() {
        return chassi.hashCode();
    }
}
