package com.estoqueveiculos.controller;

import com.estoqueveiculos.exception.VeiculoNaoEncontradoException;
import com.estoqueveiculos.model.Cliente;
import com.estoqueveiculos.model.Vendedor;
import com.estoqueveiculos.model.Venda;
import com.estoqueveiculos.service.VendaService;

import java.time.LocalDate;
import java.util.List;


public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    public Venda registrarVenda(String chassiVeiculo, Cliente cliente, Vendedor vendedor)
            throws VeiculoNaoEncontradoException {
        return vendaService.registrarVenda(chassiVeiculo, cliente, vendedor);
    }

    public List<Venda> listarVendas() {
        return vendaService.listarVendas();
    }

    public double totalVendidoNoPeriodo(LocalDate inicio, LocalDate fim) {
        return vendaService.totalVendidoNoPeriodo(inicio, fim);
    }
}
