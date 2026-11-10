package com.estoqueveiculos.repository;

import com.estoqueveiculos.exception.DadosInvalidosException;
import com.estoqueveiculos.exception.ErroPersistenciaException;
import com.estoqueveiculos.exception.VeiculoDuplicadoException;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * Repositório de veículos persistido em arquivo Properties UTF-8.
 * Reaproveita as regras do {@link VeiculoRepositoryMemoria} e, a cada alteração,
 * grava o estoque inteiro no arquivo. Se a gravação falhar, a alteração é desfeita
 * em memória para que tela e arquivo nunca fiquem diferentes.
 */
public class VeiculoRepositoryArquivo extends VeiculoRepositoryMemoria {
    private static final String TIPO_CARRO = "CARRO";
    private static final String TIPO_CAMINHONETE = "CAMINHONETE";

    private final Path arquivo;

    public VeiculoRepositoryArquivo(Path arquivo) {
        if (arquivo == null) throw new DadosInvalidosException("O caminho do arquivo é obrigatório.");
        this.arquivo = arquivo.toAbsolutePath().normalize();
        carregar();
    }

    @Override
    public void salvar(Veiculo veiculo) {
        Map<String, Veiculo> anterior = copiarDados();
        super.salvar(veiculo);
        gravarOuDesfazer(anterior);
    }

    @Override
    public void atualizar(Veiculo veiculo) throws VeiculoNaoEncontradoException {
        Map<String, Veiculo> anterior = copiarDados();
        super.atualizar(veiculo);
        gravarOuDesfazer(anterior);
    }

    @Override
    public void remover(String chassi) throws VeiculoNaoEncontradoException {
        Map<String, Veiculo> anterior = copiarDados();
        super.remover(chassi);
        gravarOuDesfazer(anterior);
    }

    // ---------- Leitura ----------

    private void carregar() {
        if (!Files.exists(arquivo)) return;

        Properties propriedades = new Properties();
        try (Reader reader = Files.newBufferedReader(arquivo)) {
            propriedades.load(reader);
            int quantidade = Integer.parseInt(propriedades.getProperty("quantidade", "0"));
            if (quantidade < 0) throw new DadosInvalidosException("quantidade de veículos inválida");
            for (int i = 0; i < quantidade; i++) {
                super.salvar(lerVeiculo(propriedades, i)); // super: não regrava o arquivo durante a leitura
            }
        } catch (IOException ex) {
            throw new ErroPersistenciaException("Não foi possível ler o arquivo de veículos: " + arquivo, ex);
        } catch (IllegalArgumentException | VeiculoDuplicadoException ex) {
            // NumberFormatException e DadosInvalidosException são subclasses de IllegalArgumentException
            throw new ErroPersistenciaException(
                    "O arquivo de veículos está corrompido (" + ex.getMessage() + "): " + arquivo, ex);
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
            case TIPO_CARRO -> new Carro(chassi, modelo, marca, ano, preco, Integer.parseInt(detalhe));
            case TIPO_CAMINHONETE -> new Caminhonete(chassi, modelo, marca, ano, preco, Double.parseDouble(detalhe));
            default -> throw new DadosInvalidosException("tipo de veículo desconhecido no registro " + indice);
        };
    }

    private String obrigatoria(Properties propriedades, String chave) {
        String valor = propriedades.getProperty(chave);
        if (valor == null) throw new DadosInvalidosException("campo ausente: " + chave);
        return valor;
    }

    // ---------- Escrita ----------

    private void gravarOuDesfazer(Map<String, Veiculo> estadoAnterior) {
        try {
            gravar(listarTodos());
        } catch (ErroPersistenciaException ex) {
            restaurarDados(estadoAnterior);
            throw ex;
        }
    }

    /** Grava num arquivo temporário e depois o move por cima do original, para nunca deixar um arquivo pela metade. */
    private void gravar(List<Veiculo> veiculos) {
        Path temporario = null;
        try {
            Path pasta = arquivo.getParent();
            if (pasta != null) Files.createDirectories(pasta);
            temporario = Files.createTempFile(pasta, "veiculos-", ".tmp");
            try (Writer writer = Files.newBufferedWriter(temporario)) {
                serializar(veiculos).store(writer, "Estoque de veículos - AutoStock");
            }
            try {
                Files.move(temporario, arquivo, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException ex) {
                Files.move(temporario, arquivo, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ex) {
            throw new ErroPersistenciaException("Não foi possível salvar os veículos no arquivo: " + arquivo, ex);
        } finally {
            if (temporario != null) {
                try { Files.deleteIfExists(temporario); } catch (IOException ignored) { /* arquivo temporário será limpo depois */ }
            }
        }
    }

    private Properties serializar(List<Veiculo> veiculos) {
        Properties propriedades = new Properties();
        propriedades.setProperty("quantidade", String.valueOf(veiculos.size()));
        int indice = 0;
        for (Veiculo veiculo : veiculos) {
            String prefixo = "veiculo." + indice++ + ".";
            propriedades.setProperty(prefixo + "tipo", veiculo.getTipo().toUpperCase(Locale.ROOT));
            propriedades.setProperty(prefixo + "chassi", veiculo.getChassi());
            propriedades.setProperty(prefixo + "marca", veiculo.getMarca());
            propriedades.setProperty(prefixo + "modelo", veiculo.getModelo());
            propriedades.setProperty(prefixo + "ano", String.valueOf(veiculo.getAnoFabricacao()));
            propriedades.setProperty(prefixo + "preco", String.valueOf(veiculo.getPreco()));
            propriedades.setProperty(prefixo + "detalhe", detalheBruto(veiculo));
        }
        return propriedades;
    }

    private static String detalheBruto(Veiculo veiculo) {
        if (veiculo instanceof Carro carro) return String.valueOf(carro.getNumeroPortas());
        if (veiculo instanceof Caminhonete caminhonete) return String.valueOf(caminhonete.getCapacidadeCargaKg());
        throw new DadosInvalidosException("Tipo de veículo não suportado: " + veiculo.getClass().getSimpleName());
    }
}
