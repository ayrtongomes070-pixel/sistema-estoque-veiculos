package com.estoqueveiculos.service;

import com.estoqueveiculos.exception.VeiculoDuplicadoException;
import com.estoqueveiculos.exception.VeiculoNaoEncontradoException;
import com.estoqueveiculos.model.Caminhonete;
import com.estoqueveiculos.model.Carro;
import com.estoqueveiculos.model.Veiculo;
import com.estoqueveiculos.repository.VeiculoRepositoryMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Testes das regras de negócio do estoque, usando o repositório em memória. */
@DisplayName("Serviço de estoque")
class EstoqueServiceTest {
    private EstoqueService servico;

    @BeforeEach
    void configurar() {
        servico = new EstoqueService(new VeiculoRepositoryMemoria());
    }

    /** Estoque de exemplo: duas marcas Volkswagen, uma Ford. */
    private void cadastrarExemplos() {
        servico.cadastrarVeiculo(new Carro("CARRO-1", "Polo", "Volkswagen", 2024, 90000, 4));
        servico.cadastrarVeiculo(new Carro("CARRO-2", "Gol", "Volkswagen", 2023, 60000, 2));
        servico.cadastrarVeiculo(new Caminhonete("CAMINHONETE-1", "Ranger", "Ford", 2024, 230000, 1100));
    }

    @Test
    @DisplayName("calcula o valor total e atualiza após remoção")
    void calculaValorTotalEPermiteRemover() throws Exception {
        cadastrarExemplos();
        assertEquals(380000, servico.valorTotalEstoque(), 0.01);

        servico.removerVeiculo("CAMINHONETE-1");
        assertEquals(2, servico.listarVeiculos().size());
        assertEquals(150000, servico.valorTotalEstoque(), 0.01);
    }

    @Test
    @DisplayName("valor total é zero com estoque vazio")
    void valorTotalZeroQuandoVazio() {
        assertEquals(0, servico.valorTotalEstoque(), 0.01);
    }

    @Test
    @DisplayName("pesquisa por marca, modelo ou chassi ignorando maiúsculas")
    void pesquisaPorMarcaModeloOuChassi() {
        cadastrarExemplos();
        assertEquals(2, servico.pesquisar("volks").size());
        assertEquals("Ranger", servico.pesquisar("RANGER").get(0).getModelo());
        assertEquals(1, servico.pesquisar("carro-2").size());
        assertTrue(servico.pesquisar("Fiat").isEmpty());
    }

    @Test
    @DisplayName("pesquisa vazia ou nula devolve todos os veículos")
    void pesquisaVaziaDevolveTodos() {
        cadastrarExemplos();
        assertEquals(3, servico.pesquisar("").size());
        assertEquals(3, servico.pesquisar(null).size());
    }

    @Test
    @DisplayName("lista marcas sem repetição e em ordem alfabética (Set)")
    void listaMarcasSemRepeticao() {
        cadastrarExemplos();
        Set<String> marcas = servico.listarMarcas();
        assertEquals(List.of("Ford", "Volkswagen"), List.copyOf(marcas));
    }

    @Test
    @DisplayName("conta veículos por tipo (Map)")
    void contaVeiculosPorTipo() {
        cadastrarExemplos();
        Map<String, Long> porTipo = servico.quantidadePorTipo();
        assertEquals(2L, porTipo.get("Carro"));
        assertEquals(1L, porTipo.get("Caminhonete"));
    }

    @Test
    @DisplayName("atualiza os dados de um veículo existente")
    void atualizaVeiculo() throws Exception {
        cadastrarExemplos();
        servico.atualizarVeiculo(new Carro("CARRO-1", "Polo Track", "Volkswagen", 2025, 95000, 4));
        Veiculo atualizado = servico.buscarPorChassi("CARRO-1");
        assertEquals("Polo Track", atualizado.getModelo());
        assertEquals(95000, atualizado.getPreco(), 0.01);
    }

    @Test
    @DisplayName("propaga as exceções de chassi duplicado e inexistente")
    void propagaExcecoes() {
        cadastrarExemplos();
        assertThrows(VeiculoDuplicadoException.class,
                () -> servico.cadastrarVeiculo(new Carro("CARRO-1", "Onix", "Chevrolet", 2024, 80000, 4)));
        assertThrows(VeiculoNaoEncontradoException.class, () -> servico.removerVeiculo("INEXISTENTE"));
        assertThrows(VeiculoNaoEncontradoException.class, () -> servico.buscarPorChassi("INEXISTENTE"));
    }
}
