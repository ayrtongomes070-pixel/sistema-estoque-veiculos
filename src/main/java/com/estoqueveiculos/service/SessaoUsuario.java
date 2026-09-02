package com.estoqueveiculos.service;

import com.estoqueveiculos.model.Vendedor;

/**
 * Mantém o vendedor atualmente logado no sistema (sessão simples, em memória).
 */
public class SessaoUsuario {

    private static Vendedor vendedorLogado;

    private SessaoUsuario() {
    }

    public static void login(Vendedor vendedor) {
        vendedorLogado = vendedor;
    }

    public static void logout() {
        vendedorLogado = null;
    }

    public static Vendedor getVendedorLogado() {
        return vendedorLogado;
    }

    public static boolean estaLogado() {
        return vendedorLogado != null;
    }
}
