package com.estoqueveiculos.service;

import com.estoqueveiculos.model.Vendedor;

/** Mantém a referência ao vendedor autenticado durante a execução. */
public class SessaoUsuario {

    private static Vendedor vendedorLogado;

    private SessaoUsuario() {
    }

    /** Define o vendedor atualmente autenticado. */
    public static void login(Vendedor vendedor) {
        vendedorLogado = vendedor;
    }

    /** Encerra a sessão atual. */
    public static void logout() {
        vendedorLogado = null;
    }

    /** Retorna o vendedor da sessão ou {@code null} se ninguém estiver autenticado. */
    public static Vendedor getVendedorLogado() {
        return vendedorLogado;
    }

    /** Informa se existe um vendedor autenticado. */
    public static boolean estaLogado() {
        return vendedorLogado != null;
    }
}
