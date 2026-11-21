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
    private String caminhoImagem;

    /**
     * Inicializa os dados comuns a todos os veículos, validando cada campo.
     * @param chassi chassi que identifica o veículo
     * @param modelo modelo do veículo
     * @param marca marca do veículo
     * @param anoFabricacao ano de fabricação
     * @param preco preço em reais (não negativo)
     * @throws com.estoqueveiculos.exception.DadosInvalidosException se algum valor for inválido
     */
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

    /**
     * Calcula o desconto comercial definido pelo subtipo do veículo.
     * @return valor do desconto em reais
     */
    public abstract double calcularDesconto();

    /**
     * Nome do tipo exibido na interface (ex.: "Carro").
     * @return nome do tipo
     */
    public abstract String getTipo();

    /**
     * Descrição do atributo específico do tipo (ex.: "4 portas", "1100 kg").
     * @return descrição do detalhe
     */
    public abstract String getDescricaoDetalhe();

    /**
     * Retorna o chassi do veículo.
     * @return o chassi do veículo
     */
    public String getChassi() {
        return chassi;
    }

    /**
     * Altera o chassi após validar e remover espaços externos.
     * @param chassi chassi que identifica o veículo
     */
    public void setChassi(String chassi) {
        validarTexto(chassi, "Chassi");
        this.chassi = chassi.trim();
    }

    /**
     * Retorna o modelo do veículo.
     * @return o modelo do veículo
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * Altera o modelo após validar e remover espaços externos.
     * @param modelo modelo do veículo
     */
    public void setModelo(String modelo) {
        validarTexto(modelo, "Modelo");
        this.modelo = modelo.trim();
    }

    /**
     * Retorna a marca do veículo.
     * @return a marca do veículo
     */
    public String getMarca() {
        return marca;
    }

    /**
     * Altera a marca após validar e remover espaços externos.
     * @param marca marca do veículo
     */
    public void setMarca(String marca) {
        validarTexto(marca, "Marca");
        this.marca = marca.trim();
    }

    /**
     * Retorna o ano de fabricação.
     * @return o ano de fabricação
     */
    public int getAnoFabricacao() {
        return anoFabricacao;
    }

    /**
     * Altera o ano de fabricação dentro do intervalo permitido.
     * @param anoFabricacao ano de fabricação
     */
    public void setAnoFabricacao(int anoFabricacao) {
        validarAno(anoFabricacao);
        this.anoFabricacao = anoFabricacao;
    }

    /**
     * Retorna o preço do veículo.
     * @return o preço do veículo
     */
    public double getPreco() {
        return preco;
    }

    /**
     * Altera o preço após validar que seja finito e não negativo.
     * @param preco preço em reais (não negativo)
     */
    public void setPreco(double preco) {
        validarPreco(preco);
        this.preco = preco;
    }

    /**
     * Retorna o caminho relativo da imagem associada ao veículo, se houver.
     * @return caminho relativo da imagem ou {@code null} quando não há foto
     */
    public String getCaminhoImagem() {
        return caminhoImagem;
    }

    /**
     * Define o caminho relativo da imagem associada ao veículo.
     * @param caminhoImagem caminho relativo da imagem, ou {@code null} para remover a associação
     */
    public void setCaminhoImagem(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem == null || caminhoImagem.isBlank()
                ? null : caminhoImagem.trim();
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

    /**
     * Retorna uma descrição textual do veículo.
     * @return uma descrição textual do veículo
     */
    @Override
    public String toString() {
        return String.format("%s %s (%d) - Chassi: %s - R$ %.2f",
                marca, modelo, anoFabricacao, chassi, preco);
    }

    /**
     * Compara veículos pelo chassi.
     * @param o o
     * @return {@code true} se os objetos tiverem a mesma identificação
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Veiculo)) return false;
        return chassi.equals(((Veiculo) o).chassi);
    }

    /**
     * Calcula o hash usando o chassi.
     * @return código hash do objeto
     */
    @Override
    public int hashCode() {
        return chassi.hashCode();
    }
}
