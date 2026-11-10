package com.estoqueveiculos.model;

import com.estoqueveiculos.exception.DadosInvalidosException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Testes das validações e do polimorfismo das entidades Carro e Caminhonete. */
@DisplayName("Modelo: Veículo, Carro e Caminhonete")
class VeiculoTest {

    @Test
    @DisplayName("carro tem 5% de desconto e caminhonete 3% (polimorfismo)")
    void descontoPorTipo() {
        Veiculo carro = new Carro("C1", "Polo", "Volkswagen", 2024, 100000, 4);
        Veiculo caminhonete = new Caminhonete("C2", "Ranger", "Ford", 2024, 100000, 1000);
        assertEquals(5000, carro.calcularDesconto(), 0.01);
        assertEquals(3000, caminhonete.calcularDesconto(), 0.01);
    }

    @Test
    @DisplayName("cada tipo informa seu nome e detalhe específico")
    void tipoEDetalhe() {
        assertEquals("Carro", new Carro("C1", "Polo", "Volkswagen", 2024, 1, 4).getTipo());
        assertEquals("4 portas", new Carro("C1", "Polo", "Volkswagen", 2024, 1, 4).getDescricaoDetalhe());
        assertEquals("Caminhonete", new Caminhonete("C2", "Ranger", "Ford", 2024, 1, 1100).getTipo());
        assertEquals("1100 kg", new Caminhonete("C2", "Ranger", "Ford", 2024, 1, 1100).getDescricaoDetalhe());
    }

    @Test
    @DisplayName("rejeita chassi, marca ou modelo vazios com mensagem do campo")
    void rejeitaTextosVazios() {
        DadosInvalidosException erro = assertThrows(DadosInvalidosException.class,
                () -> new Carro(" ", "Polo", "Volkswagen", 2024, 1000, 4));
        assertEquals("Chassi não pode ser vazio.", erro.getMessage());
        assertThrows(DadosInvalidosException.class, () -> new Carro("C1", "", "Volkswagen", 2024, 1000, 4));
        assertThrows(DadosInvalidosException.class, () -> new Carro("C1", "Polo", null, 2024, 1000, 4));
    }

    @Test
    @DisplayName("rejeita preço negativo e ano fora do intervalo")
    void rejeitaPrecoEAnoInvalidos() {
        assertThrows(DadosInvalidosException.class, () -> new Carro("C1", "Polo", "VW", 2024, -1, 4));
        assertThrows(DadosInvalidosException.class, () -> new Carro("C1", "Polo", "VW", 1800, 1000, 4));
        DadosInvalidosException erro = assertThrows(DadosInvalidosException.class,
                () -> new Carro("C1", "Polo", "VW", 3000, 1000, 4));
        assertTrue(erro.getMessage().startsWith("Ano de fabricação deve estar entre"));
    }

    @Test
    @DisplayName("rejeita número de portas e capacidade de carga inválidos")
    void rejeitaAtributosEspecificosInvalidos() {
        assertThrows(DadosInvalidosException.class, () -> new Carro("C1", "Polo", "VW", 2024, 1000, 0));
        assertThrows(DadosInvalidosException.class, () -> new Caminhonete("C2", "Ranger", "Ford", 2024, 1000, -5));
        Caminhonete caminhonete = new Caminhonete("C2", "Ranger", "Ford", 2024, 1000, 500);
        assertThrows(DadosInvalidosException.class, () -> caminhonete.setCapacidadeCargaKg(Double.NaN));
    }

    @Test
    @DisplayName("remove espaços extras dos textos")
    void removeEspacosExtras() {
        Carro carro = new Carro("  C1 ", " Polo ", " Volkswagen ", 2024, 1000, 4);
        assertEquals("C1", carro.getChassi());
        assertEquals("Polo", carro.getModelo());
        assertEquals("Volkswagen", carro.getMarca());
    }

    @Test
    @DisplayName("veículos são iguais quando têm o mesmo chassi")
    void igualdadePorChassi() {
        Veiculo a = new Carro("C1", "Polo", "Volkswagen", 2024, 1000, 4);
        Veiculo b = new Carro("C1", "Gol", "Volkswagen", 2020, 500, 2);
        Veiculo c = new Carro("C2", "Polo", "Volkswagen", 2024, 1000, 4);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }
}
