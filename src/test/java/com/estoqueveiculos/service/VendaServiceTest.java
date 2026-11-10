package com.estoqueveiculos.service;

import com.estoqueveiculos.exception.DadosInvalidosException;
import com.estoqueveiculos.exception.VeiculoNaoEncontradoException;
import com.estoqueveiculos.model.Carro;
import com.estoqueveiculos.model.Cliente;
import com.estoqueveiculos.model.Vendedor;
import com.estoqueveiculos.repository.VeiculoRepositoryMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Testes do registro de vendas e do total vendido por período. */
@DisplayName("Serviço de vendas")
class VendaServiceTest {
    private static final Cliente CLIENTE = new Cliente("123", "Ana", "81999999999", "ana@example.com");
    private static final Vendedor VENDEDOR = new Vendedor(1, "João", "joao", "senha");

    private VeiculoRepositoryMemoria repositorio;
    private VendaService servico;

    @BeforeEach
    void configurar() {
        repositorio = new VeiculoRepositoryMemoria();
        servico = new VendaService(repositorio);
    }

    @Test
    @DisplayName("registra a venda com desconto, remove o veículo do estoque e soma o período")
    void registraVendaRemoveVeiculoECalculaTotalDoPeriodo() throws Exception {
        repositorio.salvar(new Carro("CARRO-1", "Polo", "Volkswagen", 2024, 100000, 4));
        servico.registrarVenda("CARRO-1", CLIENTE, VENDEDOR);

        assertEquals(1, servico.listarVendas().size());
        assertEquals(95000, servico.totalVendidoNoPeriodo(LocalDate.now(), LocalDate.now()), 0.01);
        assertEquals(0, repositorio.listarTodos().size());
    }

    @Test
    @DisplayName("não registra venda de veículo inexistente")
    void naoRegistraVendaParaVeiculoInexistente() {
        assertThrows(VeiculoNaoEncontradoException.class,
                () -> servico.registrarVenda("INEXISTENTE", CLIENTE, VENDEDOR));
        assertEquals(0, servico.listarVendas().size());
    }

    @Test
    @DisplayName("venda sem cliente é rejeitada e o veículo continua no estoque")
    void vendaSemClienteNaoRemoveVeiculo() {
        repositorio.salvar(new Carro("CARRO-1", "Polo", "Volkswagen", 2024, 100000, 4));
        assertThrows(DadosInvalidosException.class, () -> servico.registrarVenda("CARRO-1", null, VENDEDOR));
        assertEquals(1, repositorio.listarTodos().size());
    }

    @Test
    @DisplayName("rejeita período com data final anterior à inicial")
    void rejeitaPeriodoInvertido() {
        assertThrows(DadosInvalidosException.class,
                () -> servico.totalVendidoNoPeriodo(LocalDate.now(), LocalDate.now().minusDays(1)));
    }
}
