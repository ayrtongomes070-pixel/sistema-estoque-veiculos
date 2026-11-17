package com.estoqueveiculos.util;

import com.estoqueveiculos.exception.DadosInvalidosException;

import java.util.Locale;

/**
 * Converte os textos digitados nos formulários em números, gerando mensagens
 * compreensíveis para o usuário (em vez de "For input string: ...").
 * Aceita tanto o formato brasileiro ("85.000,50") quanto o com ponto decimal ("85000.50").
 */
public final class ConversorNumero {

    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");

    private ConversorNumero() {
    }

    /** Converte um texto em inteiro, informando o nome do campo em caso de erro.
     * @param texto conteúdo digitado
     * @param campo nome do campo para a mensagem de validação
     * @return número inteiro convertido
     * @throws DadosInvalidosException se o campo estiver vazio ou não representar um inteiro
     */
    public static int paraInteiro(String texto, String campo) {
        String valor = normalizar(texto, campo);
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException ex) {
            throw new DadosInvalidosException(campo + " deve ser um número inteiro. Valor informado: \"" + valor + "\".");
        }
    }

    /** Converte um texto em decimal, aceitando formatos brasileiro e com ponto decimal.
     * @param texto conteúdo digitado
     * @param campo nome do campo para a mensagem de validação
     * @return número decimal convertido
     * @throws DadosInvalidosException se o campo estiver vazio ou não representar um número
     */
    public static double paraDecimal(String texto, String campo) {
        String valor = normalizar(texto, campo);
        String convertido = valor.contains(",")
                ? valor.replace(".", "").replace(',', '.') // formato brasileiro: ponto separa milhar
                : valor;                                    // formato com ponto decimal
        try {
            return Double.parseDouble(convertido);
        } catch (NumberFormatException ex) {
            throw new DadosInvalidosException(campo + " deve ser um número (ex.: 85000,00). Valor informado: \"" + valor + "\".");
        }
    }

    /** Formata um decimal para exibição num campo editável (ex.: 89900.0 -> "89900,00"). */
    public static String paraTexto(double valor) {
        return String.format(PT_BR, "%.2f", valor);
    }

    private static String normalizar(String texto, String campo) {
        if (texto == null || texto.isBlank()) throw new DadosInvalidosException(campo + " é obrigatório.");
        return texto.trim();
    }
}
