package com.estoqueveiculos.service;

import com.estoqueveiculos.exception.VeiculoNaoEncontradoException;
import com.estoqueveiculos.model.Carro;
import com.estoqueveiculos.model.Cliente;
import com.estoqueveiculos.model.Vendedor;
import com.estoqueveiculos.repository.VeiculoRepositoryMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VendaServiceTest {
    private VeiculoRepositoryMemoria repositorio;
    private VendaService servico;

    @BeforeEach
    void configurar() {
        repositorio = new VeiculoRepositoryMemoria();
        servico = new VendaService(repositorio);
    }

    @Test
    void registraVendaRemoveVeiculoECalculaTotalDoPeriodo() throws Exception {
        repositorio.salvar(new Carro("CARRO-1", "Polo", "Volkswagen", 2024, 100000, 4));
        servico.registrarVenda("CARRO-1",
                new Cliente("123", "Ana", "81999999999", "ana@example.com"),
                new Vendedor(1, "João", "joao", "senha"));

        assertEquals(1, servico.listarVendas().size());
        assertEquals(95000, servico.totalVendidoNoPeriodo(LocalDate.now(), LocalDate.now()), 0.01);
        assertEquals(0, repositorio.listarTodos().size());
    }

    @Test
    void naoRegistraVendaParaVeiculoInexistente() {
        assertThrows(VeiculoNaoEncontradoException.class, () -> servico.registrarVenda("INEXISTENTE",
                new Cliente("123", "Ana", "81999999999", "ana@example.com"),
                new Vendedor(1, "João", "joao", "senha")));
    }

    @Test
    void rejeitaPeriodoInvertido() {
        assertThrows(IllegalArgumentException.class,
                () -> servico.totalVendidoNoPeriodo(LocalDate.now(), LocalDate.now().minusDays(1)));
    }
}
