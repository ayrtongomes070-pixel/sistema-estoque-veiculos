package com.estoqueveiculos.service;

import com.estoqueveiculos.model.Cliente;
import com.estoqueveiculos.model.Veiculo;
import com.estoqueveiculos.model.Vendedor;
import com.estoqueveiculos.model.Venda;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Regras de negócio relacionadas a vendas de veículos.
 * TODO: persistir vendas via repository quando o banco estiver integrado.
 */
public class VendaService {

    private final List<Venda> vendas = new ArrayList<>();
    private int proximoId = 1;

    public Venda registrarVenda(Veiculo veiculo, Cliente cliente, Vendedor vendedor) {
        Venda venda = new Venda(proximoId++, veiculo, cliente, vendedor, LocalDate.now());
        vendas.add(venda);
        return venda;
    }

    public List<Venda> listarVendas() {
        return vendas;
    }
}
