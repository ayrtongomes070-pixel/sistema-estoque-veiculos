package com.estoqueveiculos.model;

import com.estoqueveiculos.exception.DadosInvalidosException;

import java.time.LocalDate;

/** Registro imutável de uma venda concluída e de suas partes associadas. */
public class Venda {

    private final int id;
    private final Veiculo veiculo;
    private final Cliente cliente;
    private final Vendedor vendedor;
    private final LocalDate dataVenda;
    private final double valorFinal;

    /** Cria uma venda e calcula seu valor final com o desconto do veículo. */
    public Venda(int id, Veiculo veiculo, Cliente cliente, Vendedor vendedor,
                 LocalDate dataVenda) {
        if (veiculo == null || cliente == null || vendedor == null || dataVenda == null) {
            throw new DadosInvalidosException("Veículo, cliente, vendedor e data da venda são obrigatórios.");
        }
        this.id = id;
        this.veiculo = veiculo;
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.dataVenda = dataVenda;
        this.valorFinal = veiculo.getPreco() - veiculo.calcularDesconto();
    }

    /** Retorna o identificador da venda. */
    public int getId() {
        return id;
    }

    /** Retorna o veículo vendido. */
    public Veiculo getVeiculo() {
        return veiculo;
    }

    /** Retorna o cliente comprador. */
    public Cliente getCliente() {
        return cliente;
    }

    /** Retorna o vendedor responsável. */
    public Vendedor getVendedor() {
        return vendedor;
    }

    /** Retorna a data da venda. */
    public LocalDate getDataVenda() {
        return dataVenda;
    }

    /** Retorna o valor final após aplicar o desconto. */
    public double getValorFinal() {
        return valorFinal;
    }

    /** Retorna um resumo da venda em formato textual. */
    @Override
    public String toString() {
        return String.format("Venda #%d - %s -> %s (R$ %.2f) em %s",
                id, veiculo.getChassi(), cliente.getNome(), valorFinal, dataVenda);
    }
}
