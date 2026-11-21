package com.estoqueveiculos.util;

import com.estoqueveiculos.exception.DadosInvalidosException;
import com.estoqueveiculos.exception.ErroPersistenciaException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/** Copia fotos escolhidas pelo usuário para a pasta local do estoque. */
public class ArmazenadorImagens {

    private static final Set<String> EXTENSOES_PERMITIDAS = Set.of(".jpg", ".jpeg", ".png", ".gif", ".bmp");
    private final Path diretorio;

    /**
     * Cria o armazenador para a pasta indicada.
     * @param diretorio pasta onde as imagens serão copiadas
     */
    public ArmazenadorImagens(Path diretorio) {
        if (diretorio == null) throw new DadosInvalidosException("A pasta de imagens é obrigatória.");
        this.diretorio = diretorio;
    }

    /**
     * Copia uma foto para o estoque e devolve seu caminho relativo ao projeto.
     * @param imagemOriginal arquivo de imagem selecionado pelo usuário
     * @return caminho relativo que pode ser salvo junto ao veículo
     * @throws DadosInvalidosException se o arquivo não existir ou tiver extensão não suportada
     * @throws ErroPersistenciaException se não for possível copiar a imagem
     */
    public String importar(Path imagemOriginal) {
        if (imagemOriginal == null || !Files.isRegularFile(imagemOriginal)) {
            throw new DadosInvalidosException("Selecione um arquivo de imagem válido.");
        }

        String nome = imagemOriginal.getFileName().toString();
        int ponto = nome.lastIndexOf('.');
        String extensao = ponto >= 0 ? nome.substring(ponto).toLowerCase(Locale.ROOT) : "";
        if (!EXTENSOES_PERMITIDAS.contains(extensao)) {
            throw new DadosInvalidosException("Use uma imagem JPG, PNG, GIF ou BMP.");
        }

        String nomeGerado = UUID.randomUUID() + extensao;
        Path destino = diretorio.resolve(nomeGerado);
        try {
            Files.createDirectories(diretorio);
            Files.copy(imagemOriginal, destino, StandardCopyOption.REPLACE_EXISTING);
            return diretorio.resolve(nomeGerado).toString().replace('\\', '/');
        } catch (IOException ex) {
            throw new ErroPersistenciaException("Não foi possível copiar a imagem para a pasta do estoque.", ex);
        }
    }

    /**
     * Resolve um caminho salvo para o caminho absoluto usado para exibir a imagem.
     * @param caminho salvo no veículo
     * @return caminho absoluto normalizado
     */
    public static Path resolver(String caminho) {
        if (caminho == null || caminho.isBlank()) return null;
        return Path.of(caminho).toAbsolutePath().normalize();
    }
}
