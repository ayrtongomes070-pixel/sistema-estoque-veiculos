package com.estoqueveiculos.controller;

import com.estoqueveiculos.exception.VeiculoNaoEncontradoException;
import com.estoqueveiculos.model.Cliente;
import com.estoqueveiculos.model.Vendedor;
import com.estoqueveiculos.model.Venda;
import com.estoqueveiculos.service.VendaService;

import java.time.LocalDate;
import java.util.List;

/** Encaminha as solicitações de venda para as regras de negócio. */
public class VendaController {

    private final VendaService vendaService;

    /** Cria o controller usando o serviço de vendas informado. */
    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    /** Registra a venda de um veículo disponível.
     * @throws VeiculoNaoEncontradoException se o chassi não estiver no estoque
     */
    public Venda registrarVenda(String chassiVeiculo, Cliente cliente, Vendedor vendedor)
            throws VeiculoNaoEncontradoException {
        return vendaService.registrarVenda(chassiVeiculo, cliente, vendedor);
    }

    /** Retorna as vendas registradas durante a execução. */
    public List<Venda> listarVendas() {
        return vendaService.listarVendas();
    }

    /** Soma os valores finais das vendas dentro do período, incluindo as datas-limite. */
    public double totalVendidoNoPeriodo(LocalDate inicio, LocalDate fim) {
        return vendaService.totalVendidoNoPeriodo(inicio, fim);
    }
}
