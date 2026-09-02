package com.estoqueveiculos.model;

import java.time.LocalDate;

public class Venda {

    private int id;
    private Veiculo veiculo;
    private Cliente cliente;
    private Vendedor vendedor;
    private LocalDate dataVenda;
    private double valorFinal;

    public Venda(int id, Veiculo veiculo, Cliente cliente, Vendedor vendedor,
                 LocalDate dataVenda) {
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

    public void setId(int id) {
        this.id = id;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }

    public double getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(double valorFinal) {
        this.valorFinal = valorFinal;
    }
}
