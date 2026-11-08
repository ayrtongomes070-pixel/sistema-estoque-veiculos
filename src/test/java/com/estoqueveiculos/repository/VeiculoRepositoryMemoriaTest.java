package com.estoqueveiculos.repository;

import com.estoqueveiculos.exception.VeiculoNaoEncontradoException;
import com.estoqueveiculos.model.Carro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VeiculoRepositoryMemoriaTest {
    private VeiculoRepositoryMemoria repositorio;

    @BeforeEach
    void configurar() {
        repositorio = new VeiculoRepositoryMemoria();
    }

    @Test
    void cadastraBuscaAtualizaERemoveVeiculo() throws Exception {
        Carro carro = new Carro("CHASSI-1", "Polo", "Volkswagen", 2024, 90000, 4);
        repositorio.salvar(carro);
        assertEquals(carro, repositorio.buscarPorChassi("CHASSI-1"));
        assertEquals(1, repositorio.listarTodos().size());

        Carro atualizado = new Carro("CHASSI-1", "Polo", "Volkswagen", 2025, 95000, 4);
        repositorio.atualizar(atualizado);
        assertEquals(2025, repositorio.buscarPorChassi("CHASSI-1").getAnoFabricacao());

        repositorio.remover("CHASSI-1");
        assertEquals(0, repositorio.listarTodos().size());
    }

    @Test
    void rejeitaChassiDuplicado() {
        repositorio.salvar(new Carro("CHASSI-1", "Polo", "Volkswagen", 2024, 90000, 4));
        assertThrows(IllegalArgumentException.class,
                () -> repositorio.salvar(new Carro("CHASSI-1", "Onix", "Chevrolet", 2024, 80000, 4)));
    }

    @Test
    void informaQuandoChassiNaoExiste() {
        assertThrows(VeiculoNaoEncontradoException.class,
                () -> repositorio.buscarPorChassi("INEXISTENTE"));
    }
}
