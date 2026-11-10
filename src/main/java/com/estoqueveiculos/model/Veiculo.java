package com.estoqueveiculos.model;

import com.estoqueveiculos.exception.DadosInvalidosException;

import java.time.Year;

/**
 * Classe abstrata que representa um veículo genérico.
 * Não deve ser instanciada diretamente — use as subclasses (Carro, Caminhonete).
 */
public abstract class Veiculo {

    /** Ano do primeiro automóvel produzido; nenhum veículo pode ser mais antigo que isso. */
    private static final int ANO_MINIMO = 1886;

    private String chassi;
    private String modelo;
    private String marca;
    private int anoFabricacao;
    private double preco;

    protected Veiculo(String chassi, String modelo, String marca, int anoFabricacao, double preco) {
        validarTexto(chassi, "Chassi");
        validarTexto(modelo, "Modelo");
        validarTexto(marca, "Marca");
        validarAno(anoFabricacao);
        validarPreco(preco);
        this.chassi = chassi.trim();
        this.modelo = modelo.trim();
        this.marca = marca.trim();
        this.anoFabricacao = anoFabricacao;
        this.preco = preco;
    }

    // Métodos abstratos — cada tipo de veículo define seu comportamento (polimorfismo)
    public abstract double calcularDesconto();

    /** Nome do tipo exibido na interface (ex.: "Carro"). */
    public abstract String getTipo();

    /** Descrição do atributo específico do tipo (ex.: "4 portas", "1100 kg"). */
    public abstract String getDescricaoDetalhe();

    public String getChassi() {
        return chassi;
    }

    public void setChassi(String chassi) {
        validarTexto(chassi, "Chassi");
        this.chassi = chassi.trim();
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        validarTexto(modelo, "Modelo");
        this.modelo = modelo.trim();
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        validarTexto(marca, "Marca");
        this.marca = marca.trim();
    }

    public int getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(int anoFabricacao) {
        validarAno(anoFabricacao);
        this.anoFabricacao = anoFabricacao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        validarPreco(preco);
        this.preco = preco;
    }

    private static void validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) throw new DadosInvalidosException(campo + " não pode ser vazio.");
    }

    private static void validarAno(int ano) {
        int anoMaximo = Year.now().getValue() + 1; // permite modelos do ano seguinte
        if (ano < ANO_MINIMO || ano > anoMaximo) {
            throw new DadosInvalidosException("Ano de fabricação deve estar entre " + ANO_MINIMO + " e " + anoMaximo + ".");
        }
    }

    private static void validarPreco(double preco) {
        if (!Double.isFinite(preco) || preco < 0) throw new DadosInvalidosException("Preço deve ser um valor válido e não negativo.");
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
