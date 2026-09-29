package com.estoqueveiculos.service;

import com.estoqueveiculos.model.Cliente;
import com.estoqueveiculos.model.Veiculo;
import com.estoqueveiculos.model.Vendedor;
import com.estoqueveiculos.model.Venda;
import com.estoqueveiculos.exception.VeiculoNaoEncontradoException;
import com.estoqueveiculos.repository.IVeiculoRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class VendaService {

    private final IVeiculoRepository veiculoRepository;
    private final List<Venda> vendas = new ArrayList<>();
    private int proximoId = 1;

    public VendaService(IVeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    public Venda registrarVenda(String chassi, Cliente cliente, Vendedor vendedor)
            throws VeiculoNaoEncontradoException {
        Veiculo veiculo = veiculoRepository.buscarPorChassi(chassi);
        Venda venda = new Venda(proximoId++, veiculo, cliente, vendedor, LocalDate.now());
        veiculoRepository.remover(chassi);
        vendas.add(venda);
        return venda;
    }

    public List<Venda> listarVendas() {
        return Collections.unmodifiableList(new ArrayList<>(vendas));
    }

    public double totalVendidoNoPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null || fim.isBefore(inicio)) {
            throw new IllegalArgumentException("Informe um período válido.");
        }
        return vendas.stream()
                .filter(venda -> !venda.getDataVenda().isBefore(inicio)
                        && !venda.getDataVenda().isAfter(fim))
                .mapToDouble(Venda::getValorFinal)
                .sum();
    }
}
