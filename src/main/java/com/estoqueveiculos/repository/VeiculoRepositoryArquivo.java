package com.estoqueveiculos.repository;

import com.estoqueveiculos.exception.ErroPersistenciaException;
import com.estoqueveiculos.exception.VeiculoNaoEncontradoException;
import com.estoqueveiculos.model.Caminhonete;
import com.estoqueveiculos.model.Carro;
import com.estoqueveiculos.model.Veiculo;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/** Repositório de veículos persistido em arquivo Properties UTF-8. */
public class VeiculoRepositoryArquivo implements IVeiculoRepository {
    private final Path arquivo;
    private Map<String, Veiculo> veiculos;

    public VeiculoRepositoryArquivo(Path arquivo) {
        if (arquivo == null) throw new IllegalArgumentException("O caminho do arquivo é obrigatório.");
        this.arquivo = arquivo.toAbsolutePath().normalize();
        this.veiculos = carregar();
    }

    @Override
    public synchronized void salvar(Veiculo veiculo) {
        validarVeiculo(veiculo);
        if (veiculos.containsKey(veiculo.getChassi())) {
            throw new IllegalArgumentException("Já existe um veículo com esse chassi.");
        }
        Map<String, Veiculo> atualizados = new LinkedHashMap<>(veiculos);
        atualizados.put(veiculo.getChassi(), veiculo);
        persistirEAtualizar(atualizados);
    }

    @Override
    public synchronized Veiculo buscarPorChassi(String chassi) throws VeiculoNaoEncontradoException {
        Veiculo veiculo = veiculos.get(chassi);
        if (veiculo == null) throw new VeiculoNaoEncontradoException(chassi);
        return veiculo;
    }

    @Override
    public synchronized List<Veiculo> listarTodos() {
        return new ArrayList<>(veiculos.values());
    }

    @Override
    public synchronized void atualizar(Veiculo veiculo) throws VeiculoNaoEncontradoException {
        validarVeiculo(veiculo);
        if (!veiculos.containsKey(veiculo.getChassi())) {
            throw new VeiculoNaoEncontradoException(veiculo.getChassi());
        }
        Map<String, Veiculo> atualizados = new LinkedHashMap<>(veiculos);
        atualizados.put(veiculo.getChassi(), veiculo);
        persistirEAtualizar(atualizados);
    }

    @Override
    public synchronized void remover(String chassi) throws VeiculoNaoEncontradoException {
        if (!veiculos.containsKey(chassi)) throw new VeiculoNaoEncontradoException(chassi);
        Map<String, Veiculo> atualizados = new LinkedHashMap<>(veiculos);
        atualizados.remove(chassi);
        persistirEAtualizar(atualizados);
    }

    private Map<String, Veiculo> carregar() {
        Map<String, Veiculo> resultado = new LinkedHashMap<>();
        if (!Files.exists(arquivo)) return resultado;

        Properties propriedades = new Properties();
        try (Reader reader = Files.newBufferedReader(arquivo)) {
            propriedades.load(reader);
            int quantidade = Integer.parseInt(propriedades.getProperty("quantidade", "0"));
            if (quantidade < 0) throw new IllegalArgumentException("Quantidade de veículos inválida.");
            for (int i = 0; i < quantidade; i++) {
                Veiculo veiculo = lerVeiculo(propriedades, i);
                if (resultado.putIfAbsent(veiculo.getChassi(), veiculo) != null) {
                    throw new IllegalArgumentException("Chassi duplicado no arquivo: " + veiculo.getChassi());
                }
            }
            return resultado;
        } catch (IOException | IllegalArgumentException ex) {
            throw new ErroPersistenciaException("Não foi possível ler o arquivo de veículos: " + arquivo, ex);
        }
    }

    private Veiculo lerVeiculo(Properties propriedades, int indice) {
        String prefixo = "veiculo." + indice + ".";
        String tipo = obrigatoria(propriedades, prefixo + "tipo");
        String chassi = obrigatoria(propriedades, prefixo + "chassi");
        String marca = obrigatoria(propriedades, prefixo + "marca");
        String modelo = obrigatoria(propriedades, prefixo + "modelo");
        int ano = Integer.parseInt(obrigatoria(propriedades, prefixo + "ano"));
        double preco = Double.parseDouble(obrigatoria(propriedades, prefixo + "preco"));
        String detalhe = obrigatoria(propriedades, prefixo + "detalhe");

        return switch (tipo) {
            case "CARRO" -> new Carro(chassi, modelo, marca, ano, preco, Integer.parseInt(detalhe));
            case "CAMINHONETE" -> new Caminhonete(chassi, modelo, marca, ano, preco, Double.parseDouble(detalhe));
            default -> throw new IllegalArgumentException("Tipo de veículo desconhecido no registro " + indice + ".");
        };
    }

    private String obrigatoria(Properties propriedades, String chave) {
        String valor = propriedades.getProperty(chave);
        if (valor == null) throw new IllegalArgumentException("Campo ausente no arquivo: " + chave);
        return valor;
    }

    private void persistirEAtualizar(Map<String, Veiculo> atualizados) {
        Path temporario = null;
        try {
            Path pasta = arquivo.getParent();
            if (pasta != null) Files.createDirectories(pasta);
            temporario = Files.createTempFile(pasta, "veiculos-", ".tmp");
            Properties propriedades = serializar(atualizados);
            try (Writer writer = Files.newBufferedWriter(temporario)) {
                propriedades.store(writer, "Estoque de veículos - StockMaster");
            }
            try {
                Files.move(temporario, arquivo, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException ex) {
                Files.move(temporario, arquivo, StandardCopyOption.REPLACE_EXISTING);
            }
            veiculos = atualizados;
        } catch (IOException ex) {
            throw new ErroPersistenciaException("Não foi possível salvar os veículos no arquivo: " + arquivo, ex);
        } finally {
            if (temporario != null) {
                try { Files.deleteIfExists(temporario); } catch (IOException ignored) { /* arquivo temporário será limpo depois */ }
            }
        }
    }

    private Properties serializar(Map<String, Veiculo> origem) {
        Properties propriedades = new Properties();
        propriedades.setProperty("quantidade", String.valueOf(origem.size()));
        int indice = 0;
        for (Veiculo veiculo : origem.values()) {
            String prefixo = "veiculo." + indice++ + ".";
            propriedades.setProperty(prefixo + "tipo", veiculo instanceof Carro ? "CARRO" : "CAMINHONETE");
            propriedades.setProperty(prefixo + "chassi", veiculo.getChassi());
            propriedades.setProperty(prefixo + "marca", veiculo.getMarca());
            propriedades.setProperty(prefixo + "modelo", veiculo.getModelo());
            propriedades.setProperty(prefixo + "ano", String.valueOf(veiculo.getAnoFabricacao()));
            propriedades.setProperty(prefixo + "preco", String.valueOf(veiculo.getPreco()));
            String detalhe = veiculo instanceof Carro carro
                    ? String.valueOf(carro.getNumeroPortas())
                    : String.valueOf(((Caminhonete) veiculo).getCapacidadeCargaKg());
            propriedades.setProperty(prefixo + "detalhe", detalhe);
        }
        return propriedades;
    }

    private void validarVeiculo(Veiculo veiculo) {
        if (veiculo == null) throw new IllegalArgumentException("Veículo não pode ser nulo.");
    }
}
