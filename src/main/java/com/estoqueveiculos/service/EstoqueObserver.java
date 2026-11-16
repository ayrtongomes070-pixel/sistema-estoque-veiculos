package com.estoqueveiculos.service;

/**
 * Observador notificado depois que os veículos disponíveis no estoque mudam.
 * Implementa o papel de Observer do padrão GoF.
 */
@FunctionalInterface
public interface EstoqueObserver {
    /** Executa uma ação em resposta a uma alteração confirmada no estoque. */
    void aoAlterarEstoque();
}
