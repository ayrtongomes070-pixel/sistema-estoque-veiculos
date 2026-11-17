package com.estoqueveiculos.factory;

import com.estoqueveiculos.exception.DadosInvalidosException;
import com.estoqueveiculos.model.Veiculo;
import com.estoqueveiculos.util.ConversorNumero;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Criador abstrato do padrão GoF <b>Factory Method</b> para veículos.
 * <p>
 * O sistema recebe veículos como texto em dois lugares: no formulário da interface
 * e na leitura do arquivo {@code dados/veiculos.properties}. Esta classe concentra a
 * conversão dos dados comuns (ano e preço) e delega a instanciação do tipo concreto
 * ao método-fábrica {@link #instanciar}, implementado por cada subclasse
 * ({@link CarroFactory}, {@link CaminhoneteFactory}).
 * <p>
 * Para incluir um novo tipo de veículo basta criar uma nova subclasse e registrá-la em
 * {@link #FABRICAS}; a interface gráfica e o repositório em arquivo passam a aceitá-lo
 * sem nenhum {@code switch} ou {@code instanceof} adicional.
 */
public abstract class VeiculoFactory {

    /** Fábricas conhecidas, na ordem em que os tipos aparecem na interface. */
    private static final List<VeiculoFactory> FABRICAS = List.of(new CarroFactory(), new CaminhoneteFactory());

    /** Construtor disponível apenas para as fábricas concretas. */
    protected VeiculoFactory() {
    }

    /**
     * Retorna a fábrica responsável pelo tipo informado, sem diferenciar maiúsculas e minúsculas.
     *
     * @param tipo nome do tipo (ex.: "Carro", "CAMINHONETE")
     * @return fábrica do tipo
     * @throws DadosInvalidosException se o tipo for nulo ou desconhecido
     */
    public static VeiculoFactory paraTipo(String tipo) {
        if (tipo == null) throw new DadosInvalidosException("Tipo de veículo é obrigatório.");
        String procurado = tipo.trim();
        return FABRICAS.stream()
                .filter(f -> f.getTipo().equalsIgnoreCase(procurado))
                .findFirst()
                .orElseThrow(() -> new DadosInvalidosException("Tipo de veículo desconhecido: \"" + tipo + "\"."));
    }

    /**
     * Retorna a fábrica capaz de criar (e descrever) o veículo informado.
     *
     * @param veiculo veículo existente
     * @return fábrica correspondente ao tipo do veículo
     * @throws DadosInvalidosException se o veículo for nulo ou de um tipo sem fábrica registrada
     */
    public static VeiculoFactory paraVeiculo(Veiculo veiculo) {
        if (veiculo == null) throw new DadosInvalidosException("Veículo não pode ser nulo.");
        return paraTipo(veiculo.getTipo());
    }

    /**
     * Lista os nomes dos tipos de veículo que podem ser criados.
     *
     * @return nomes dos tipos, na ordem de exibição
     */
    public static List<String> tiposDisponiveis() {
        return FABRICAS.stream().map(VeiculoFactory::getTipo).collect(Collectors.toUnmodifiableList());
    }

    /**
     * Cria um veículo a partir de valores em texto, como os digitados no formulário
     * ou lidos do arquivo. Ano e preço são convertidos aqui; o detalhe específico é
     * interpretado pela fábrica concreta.
     *
     * @param chassi  identificador do veículo
     * @param modelo  modelo do veículo
     * @param marca   marca do veículo
     * @param ano     ano de fabricação em texto
     * @param preco   preço em texto (aceita "85000,50" ou "85000.50")
     * @param detalhe valor do atributo específico do tipo, em texto
     * @return novo veículo do tipo desta fábrica
     * @throws DadosInvalidosException se algum valor estiver vazio ou inválido
     */
    public final Veiculo criar(String chassi, String modelo, String marca, String ano, String preco, String detalhe) {
        int anoFabricacao = ConversorNumero.paraInteiro(ano, "Ano");
        double valor = ConversorNumero.paraDecimal(preco, "Preço");
        return instanciar(chassi, modelo, marca, anoFabricacao, valor, detalhe);
    }

    /**
     * Método-fábrica: instancia o veículo concreto, convertendo o detalhe específico do tipo.
     *
     * @param chassi        identificador do veículo
     * @param modelo        modelo do veículo
     * @param marca         marca do veículo
     * @param anoFabricacao ano de fabricação já convertido
     * @param preco         preço já convertido
     * @param detalhe       valor do atributo específico, em texto
     * @return novo veículo
     */
    protected abstract Veiculo instanciar(String chassi, String modelo, String marca,
                                          int anoFabricacao, double preco, String detalhe);

    /**
     * Nome do tipo criado por esta fábrica; igual ao {@link Veiculo#getTipo()} do produto.
     *
     * @return nome do tipo (ex.: "Carro")
     */
    public abstract String getTipo();

    /**
     * Rótulo do atributo específico do tipo, usado no formulário.
     *
     * @return rótulo do campo de detalhe (ex.: "Número de portas")
     */
    public abstract String getRotuloDetalhe();

    /**
     * Converte o atributo específico do veículo em texto, no formato aceito por {@link #criar}.
     * É o caminho inverso da criação: usado para preencher o formulário de edição e para gravar o arquivo.
     *
     * @param veiculo veículo do tipo desta fábrica
     * @return detalhe em texto
     * @throws DadosInvalidosException se o veículo não for do tipo desta fábrica
     */
    public abstract String detalheComoTexto(Veiculo veiculo);

    /**
     * Garante que o veículo recebido é do tipo produzido pela fábrica.
     *
     * @param veiculo veículo a verificar
     * @param classe  classe concreta esperada
     * @param <T>     tipo concreto
     * @return o veículo convertido para o tipo concreto
     * @throws DadosInvalidosException se o veículo for de outro tipo
     */
    protected <T extends Veiculo> T exigirTipo(Veiculo veiculo, Class<T> classe) {
        if (!classe.isInstance(veiculo)) {
            throw new DadosInvalidosException("A fábrica de " + getTipo() + " não aceita o veículo informado.");
        }
        return classe.cast(veiculo);
    }
}
