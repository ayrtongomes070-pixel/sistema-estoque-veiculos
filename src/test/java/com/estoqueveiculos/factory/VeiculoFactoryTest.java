package com.estoqueveiculos.factory;

import com.estoqueveiculos.exception.DadosInvalidosException;
import com.estoqueveiculos.model.Caminhonete;
import com.estoqueveiculos.model.Carro;
import com.estoqueveiculos.model.Veiculo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Testes do padrão Factory Method usado na criação de veículos. */
@DisplayName("Fábrica de veículos (Factory Method)")
class VeiculoFactoryTest {

    @Test
    @DisplayName("escolhe a fábrica pelo tipo, sem diferenciar maiúsculas e minúsculas")
    void escolheFabricaPeloTipo() {
        assertInstanceOf(CarroFactory.class, VeiculoFactory.paraTipo("carro"));
        assertInstanceOf(CaminhoneteFactory.class, VeiculoFactory.paraTipo("CAMINHONETE"));
        assertEquals(List.of("Carro", "Caminhonete"), VeiculoFactory.tiposDisponiveis());
    }

    @Test
    @DisplayName("cria carro e caminhonete a partir de textos do formulário")
    void criaVeiculosAPartirDeTexto() {
        Veiculo carro = VeiculoFactory.paraTipo("Carro").criar("C1", "Polo", "Volkswagen", "2024", "89.900,50", "4");
        Veiculo caminhonete = VeiculoFactory.paraTipo("Caminhonete").criar("C2", "Ranger", "Ford", "2025", "239900.0", "1100,5");

        Carro c = assertInstanceOf(Carro.class, carro);
        assertEquals(89900.50, c.getPreco(), 0.001);
        assertEquals(4, c.getNumeroPortas());
        Caminhonete cam = assertInstanceOf(Caminhonete.class, caminhonete);
        assertEquals(2025, cam.getAnoFabricacao());
        assertEquals(1100.5, cam.getCapacidadeCargaKg(), 0.001);
    }

    @Test
    @DisplayName("converte o detalhe em texto e de volta sem perder informação")
    void detalheComoTextoEhReversivel() {
        Veiculo original = new Caminhonete("C2", "Ranger", "Ford", 2024, 1000, 1250.75);
        VeiculoFactory fabrica = VeiculoFactory.paraVeiculo(original);
        String texto = fabrica.detalheComoTexto(original);

        Caminhonete recriada = (Caminhonete) fabrica.criar("C2", "Ranger", "Ford", "2024", "1000", texto);
        assertEquals(1250.75, recriada.getCapacidadeCargaKg(), 0.001);
        assertEquals("4", VeiculoFactory.paraTipo("Carro").detalheComoTexto(new Carro("C1", "Polo", "VW", 2024, 1, 4)));
    }

    @Test
    @DisplayName("rejeita tipo desconhecido, detalhe inválido e veículo de outro tipo")
    void rejeitaDadosInvalidos() {
        assertThrows(DadosInvalidosException.class, () -> VeiculoFactory.paraTipo("Moto"));
        assertThrows(DadosInvalidosException.class, () -> VeiculoFactory.paraTipo(null));
        DadosInvalidosException ex = assertThrows(DadosInvalidosException.class,
                () -> VeiculoFactory.paraTipo("Carro").criar("C1", "Polo", "VW", "2024", "1000", "quatro"));
        assertEquals("Número de portas deve ser um número inteiro. Valor informado: \"quatro\".", ex.getMessage());
        assertThrows(DadosInvalidosException.class,
                () -> new CarroFactory().detalheComoTexto(new Caminhonete("C2", "Ranger", "Ford", 2024, 1, 10)));
    }
}
