package com.estoqueveiculos.model;

import java.time.LocalDate;

/**
 * Representa a transação de venda de um veículo.
 * Imutável após a criação (sem setters), conforme modelagem da Etapa 2:
 * um registro de venda já concluída não deve ser alterado.
 */
public class Venda {

    private final int id;
    private final Veiculo veiculo;
    private final Cliente cliente;
    private final Vendedor vendedor;
    private final LocalDate dataVenda;
    private final double valorFinal;

    public Venda(int id, Veiculo veiculo, Cliente cliente, Vendedor vendedor,
                 LocalDate dataVenda) {
        if (veiculo == null || cliente == null || vendedor == null) {
            throw new IllegalArgumentException("Veículo, cliente e vendedor são obrigatórios.");
        }
        this.id = id;
        this.veiculo = veiculo;
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.dataVenda = dataVenda;
        this.valorFinal = veiculo.getPreco() - veiculo.calcularDesconto();
    }

    public int getId() {
        return id;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public double getValorFinal() {
        return valorFinal;
    }

    @Override
    public String toString() {
        return String.format("Venda #%d - %s -> %s (R$ %.2f) em %s",
                id, veiculo.getChassi(), cliente.getNome(), valorFinal, dataVenda);
    }
}
