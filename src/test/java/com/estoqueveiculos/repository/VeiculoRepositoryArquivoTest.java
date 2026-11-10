package com.estoqueveiculos.repository;

import com.estoqueveiculos.exception.ErroPersistenciaException;
import com.estoqueveiculos.exception.VeiculoDuplicadoException;
import com.estoqueveiculos.exception.VeiculoNaoEncontradoException;
import com.estoqueveiculos.model.Caminhonete;
import com.estoqueveiculos.model.Carro;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes de leitura e escrita do estoque em arquivo (I/O).
 * Cada teste usa uma pasta temporária criada pelo JUnit (@TempDir), sem tocar no arquivo real da aplicação.
 */
@DisplayName("Repositório em arquivo")
class VeiculoRepositoryArquivoTest {
    @TempDir
    Path pastaTemporaria;

    @Test
    @DisplayName("grava e recupera carro e caminhonete ao reabrir o arquivo")
    void gravaERecuperaCarroECaminhonete() throws Exception {
        Path arquivo = pastaTemporaria.resolve("estoque.properties");
        VeiculoRepositoryArquivo repositorio = new VeiculoRepositoryArquivo(arquivo);
        repositorio.salvar(new Carro("CARRO-1", "Polo", "Volkswagen", 2024, 90000, 4));
        repositorio.salvar(new Caminhonete("CAMINHAO-1", "Ranger", "Ford", 2025, 230000, 1100));

        VeiculoRepositoryArquivo reaberto = new VeiculoRepositoryArquivo(arquivo);
        assertEquals(2, reaberto.listarTodos().size());
        assertInstanceOf(Carro.class, reaberto.buscarPorChassi("CARRO-1"));
        assertInstanceOf(Caminhonete.class, reaberto.buscarPorChassi("CAMINHAO-1"));
        assertEquals(1100, ((Caminhonete) reaberto.buscarPorChassi("CAMINHAO-1")).getCapacidadeCargaKg(), 0.01);
    }

    @Test
    @DisplayName("persiste atualização e remoção")
    void persisteAtualizacaoERemocao() throws Exception {
        Path arquivo = pastaTemporaria.resolve("estoque.properties");
        VeiculoRepositoryArquivo repositorio = new VeiculoRepositoryArquivo(arquivo);
        repositorio.salvar(new Carro("CARRO-1", "Polo", "Volkswagen", 2024, 90000, 4));
        repositorio.atualizar(new Carro("CARRO-1", "Polo", "Volkswagen", 2025, 95000, 4));
        assertEquals(95000, new VeiculoRepositoryArquivo(arquivo).buscarPorChassi("CARRO-1").getPreco(), 0.01);

        repositorio.remover("CARRO-1");

        VeiculoRepositoryArquivo reaberto = new VeiculoRepositoryArquivo(arquivo);
        assertEquals(0, reaberto.listarTodos().size());
        assertThrows(VeiculoNaoEncontradoException.class, () -> reaberto.buscarPorChassi("CARRO-1"));
        assertEquals("quantidade=0", Files.readString(arquivo).lines()
                .filter(linha -> linha.startsWith("quantidade=")).findFirst().orElseThrow());
    }

    @Test
    @DisplayName("começa vazio quando o arquivo ainda não existe")
    void comecaVazioSemArquivo() {
        VeiculoRepositoryArquivo repositorio = new VeiculoRepositoryArquivo(pastaTemporaria.resolve("novo.properties"));
        assertTrue(repositorio.listarTodos().isEmpty());
    }

    @Test
    @DisplayName("cria a pasta de dados automaticamente na primeira gravação")
    void criaPastaAutomaticamente() {
        Path arquivo = pastaTemporaria.resolve("dados").resolve("veiculos.properties");
        new VeiculoRepositoryArquivo(arquivo).salvar(new Carro("CARRO-1", "Polo", "Volkswagen", 2024, 90000, 4));
        assertTrue(Files.exists(arquivo));
    }

    @Test
    @DisplayName("não grava nada quando o chassi é duplicado")
    void naoGravaDuplicado() {
        Path arquivo = pastaTemporaria.resolve("estoque.properties");
        VeiculoRepositoryArquivo repositorio = new VeiculoRepositoryArquivo(arquivo);
        repositorio.salvar(new Carro("CARRO-1", "Polo", "Volkswagen", 2024, 90000, 4));

        assertThrows(VeiculoDuplicadoException.class,
                () -> repositorio.salvar(new Carro("CARRO-1", "Onix", "Chevrolet", 2024, 80000, 4)));
        assertEquals(1, new VeiculoRepositoryArquivo(arquivo).listarTodos().size());
    }

    @Test
    @DisplayName("arquivo corrompido gera ErroPersistenciaException com mensagem explicativa")
    void arquivoCorrompidoGeraErroPersistencia() throws Exception {
        Path arquivo = pastaTemporaria.resolve("estoque.properties");
        Files.writeString(arquivo, "quantidade=1\nveiculo.0.tipo=CARRO\nveiculo.0.chassi=X\n"); // faltam campos

        ErroPersistenciaException erro = assertThrows(ErroPersistenciaException.class,
                () -> new VeiculoRepositoryArquivo(arquivo));
        assertTrue(erro.getMessage().contains("corrompido"));
        assertTrue(erro.getMessage().contains("veiculo.0.marca"));
    }

    @Test
    @DisplayName("número inválido no arquivo gera ErroPersistenciaException")
    void numeroInvalidoNoArquivo() throws Exception {
        Path arquivo = pastaTemporaria.resolve("estoque.properties");
        Files.writeString(arquivo, "quantidade=abc\n");
        assertThrows(ErroPersistenciaException.class, () -> new VeiculoRepositoryArquivo(arquivo));
    }

    @Test
    @DisplayName("falha ao gravar desfaz a alteração em memória")
    void falhaNaGravacaoDesfazAlteracao() throws Exception {
        // A "pasta" de dados é na verdade um arquivo comum, então não dá para criar a pasta nem gravar nela
        Path arquivoNoLugarDaPasta = Files.writeString(pastaTemporaria.resolve("dados"), "não é uma pasta");
        VeiculoRepositoryArquivo quebrado = new VeiculoRepositoryArquivo(arquivoNoLugarDaPasta.resolve("veiculos.properties"));

        assertThrows(ErroPersistenciaException.class,
                () -> quebrado.salvar(new Carro("CARRO-2", "Onix", "Chevrolet", 2024, 80000, 4)));
        assertTrue(quebrado.listarTodos().isEmpty(), "o veículo não deve ficar na memória se não foi salvo no arquivo");
    }
}
