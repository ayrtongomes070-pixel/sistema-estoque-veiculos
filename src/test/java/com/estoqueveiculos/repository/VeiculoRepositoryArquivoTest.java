package com.estoqueveiculos.repository;

import com.estoqueveiculos.exception.VeiculoNaoEncontradoException;
import com.estoqueveiculos.model.Caminhonete;
import com.estoqueveiculos.model.Carro;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VeiculoRepositoryArquivoTest {
    @TempDir
    Path pastaTemporaria;

    @Test
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
    void persisteAtualizacaoERemocao() throws Exception {
        Path arquivo = pastaTemporaria.resolve("estoque.properties");
        VeiculoRepositoryArquivo repositorio = new VeiculoRepositoryArquivo(arquivo);
        repositorio.salvar(new Carro("CARRO-1", "Polo", "Volkswagen", 2024, 90000, 4));
        repositorio.atualizar(new Carro("CARRO-1", "Polo", "Volkswagen", 2025, 95000, 4));
        repositorio.remover("CARRO-1");

        VeiculoRepositoryArquivo reaberto = new VeiculoRepositoryArquivo(arquivo);
        assertEquals(0, reaberto.listarTodos().size());
        assertThrows(VeiculoNaoEncontradoException.class, () -> reaberto.buscarPorChassi("CARRO-1"));
        assertEquals("quantidade=0", Files.readString(arquivo).lines()
                .filter(linha -> linha.startsWith("quantidade=")).findFirst().orElseThrow());
    }
}
