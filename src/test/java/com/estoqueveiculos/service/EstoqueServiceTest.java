package com.estoqueveiculos.service;

import com.estoqueveiculos.exception.VeiculoNaoEncontradoException;
import com.estoqueveiculos.model.Carro;
import com.estoqueveiculos.repository.VeiculoRepositoryMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EstoqueServiceTest {
    private EstoqueService servico;

    @BeforeEach
    void configurar() {
        servico = new EstoqueService(new VeiculoRepositoryMemoria());
    }

    @Test
    void calculaValorTotalEPermiteRemover() throws Exception {
        servico.cadastrarVeiculo(new Carro("CARRO-1", "Polo", "Volkswagen", 2024, 90000, 4));
        servico.cadastrarVeiculo(new Carro("CARRO-2", "Onix", "Chevrolet", 2024, 80000, 4));
        assertEquals(170000, servico.valorTotalEstoque(), 0.01);

        servico.removerVeiculo("CARRO-1");
        assertEquals(1, servico.listarVeiculos().size());
        assertEquals(80000, servico.valorTotalEstoque(), 0.01);
    }

    @Test
    void encaminhaErroQuandoVeiculoNaoExiste() {
        assertThrows(VeiculoNaoEncontradoException.class,
                () -> servico.removerVeiculo("INEXISTENTE"));
    }
}
