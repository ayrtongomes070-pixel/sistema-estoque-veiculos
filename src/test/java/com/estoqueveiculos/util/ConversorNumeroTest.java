package com.estoqueveiculos.util;

import com.estoqueveiculos.exception.DadosInvalidosException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Testes da conversão dos textos digitados nos formulários para números. */
@DisplayName("Conversor de números do formulário")
class ConversorNumeroTest {

    @Test
    @DisplayName("aceita decimal no formato brasileiro e com ponto")
    void aceitaFormatosDecimais() {
        assertEquals(85000.50, ConversorNumero.paraDecimal("85.000,50", "Preço"), 0.001);
        assertEquals(85000.50, ConversorNumero.paraDecimal("85000,50", "Preço"), 0.001);
        assertEquals(89900.0, ConversorNumero.paraDecimal("89900.0", "Preço"), 0.001);
        assertEquals(1100, ConversorNumero.paraDecimal(" 1100 ", "Capacidade"), 0.001);
    }

    @Test
    @DisplayName("o texto exibido na edição é lido de volta com o mesmo valor (bug do preço multiplicado por 10)")
    void idaEVoltaMantemValor() {
        double original = 89900.0;
        String exibido = ConversorNumero.paraTexto(original);
        assertEquals("89900,00", exibido);
        assertEquals(original, ConversorNumero.paraDecimal(exibido, "Preço"), 0.001);
    }

    @Test
    @DisplayName("converte inteiros")
    void converteInteiro() {
        assertEquals(2024, ConversorNumero.paraInteiro(" 2024 ", "Ano"));
    }

    @Test
    @DisplayName("texto inválido gera mensagem com o nome do campo e o valor digitado")
    void mensagemUtilParaTextoInvalido() {
        DadosInvalidosException erro = assertThrows(DadosInvalidosException.class,
                () -> ConversorNumero.paraInteiro("abc", "Ano"));
        assertTrue(erro.getMessage().contains("Ano"));
        assertTrue(erro.getMessage().contains("abc"));
        assertThrows(DadosInvalidosException.class, () -> ConversorNumero.paraDecimal("12x", "Preço"));
    }

    @Test
    @DisplayName("campo vazio é informado como obrigatório")
    void campoVazio() {
        DadosInvalidosException erro = assertThrows(DadosInvalidosException.class,
                () -> ConversorNumero.paraDecimal("  ", "Preço"));
        assertEquals("Preço é obrigatório.", erro.getMessage());
    }
}
