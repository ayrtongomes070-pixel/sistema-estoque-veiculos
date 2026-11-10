package com.estoqueveiculos.repository;

import com.estoqueveiculos.exception.DadosInvalidosException;
import com.estoqueveiculos.exception.VeiculoDuplicadoException;
import com.estoqueveiculos.exception.VeiculoNaoEncontradoException;
import com.estoqueveiculos.model.Carro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Testes do repositório em memória (CRUD e erros de chassi duplicado/inexistente). */
@DisplayName("Repositório em memória")
class VeiculoRepositoryMemoriaTest {
    private VeiculoRepositoryMemoria repositorio;

    @BeforeEach
    void configurar() {
        repositorio = new VeiculoRepositoryMemoria();
    }

    @Test
    @DisplayName("cadastra, busca, atualiza e remove um veículo")
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
    @DisplayName("rejeita chassi duplicado com mensagem que cita o chassi")
    void rejeitaChassiDuplicado() {
        repositorio.salvar(new Carro("CHASSI-1", "Polo", "Volkswagen", 2024, 90000, 4));
        VeiculoDuplicadoException erro = assertThrows(VeiculoDuplicadoException.class,
                () -> repositorio.salvar(new Carro("CHASSI-1", "Onix", "Chevrolet", 2024, 80000, 4)));
        assertTrue(erro.getMessage().contains("CHASSI-1"));
    }

    @Test
    @DisplayName("lança VeiculoNaoEncontradoException ao buscar, atualizar ou remover chassi inexistente")
    void informaQuandoChassiNaoExiste() {
        assertThrows(VeiculoNaoEncontradoException.class, () -> repositorio.buscarPorChassi("INEXISTENTE"));
        assertThrows(VeiculoNaoEncontradoException.class, () -> repositorio.remover("INEXISTENTE"));
        assertThrows(VeiculoNaoEncontradoException.class,
                () -> repositorio.atualizar(new Carro("INEXISTENTE", "Polo", "Volkswagen", 2024, 90000, 4)));
    }

    @Test
    @DisplayName("rejeita veículo nulo")
    void rejeitaVeiculoNulo() {
        assertThrows(DadosInvalidosException.class, () -> repositorio.salvar(null));
        assertThrows(DadosInvalidosException.class, () -> repositorio.atualizar(null));
    }

    @Test
    @DisplayName("devolve uma cópia da lista, protegendo os dados internos")
    void listaRetornadaEhUmaCopia() {
        repositorio.salvar(new Carro("CHASSI-1", "Polo", "Volkswagen", 2024, 90000, 4));
        repositorio.listarTodos().clear();
        assertEquals(1, repositorio.listarTodos().size());
    }
}
