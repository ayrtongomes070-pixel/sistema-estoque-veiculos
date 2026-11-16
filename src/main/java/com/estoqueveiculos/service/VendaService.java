package com.estoqueveiculos.service;

import com.estoqueveiculos.model.Cliente;
import com.estoqueveiculos.model.Veiculo;
import com.estoqueveiculos.model.Vendedor;
import com.estoqueveiculos.model.Venda;
import com.estoqueveiculos.exception.DadosInvalidosException;
import com.estoqueveiculos.exception.VeiculoNaoEncontradoException;
import com.estoqueveiculos.repository.IVeiculoRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/** Aplica as regras de registro e consulta de vendas em memória. */
public class VendaService {

    private final IVeiculoRepository veiculoRepository;
    private final List<Venda> vendas = new ArrayList<>();
    private int proximoId = 1;

    /** Cria o serviço de vendas associado à estratégia de persistência dos veículos. */
    public VendaService(IVeiculoRepository veiculoRepository) {
        if (veiculoRepository == null) throw new DadosInvalidosException("Repositório de veículos é obrigatório.");
        this.veiculoRepository = veiculoRepository;
    }

    /** Registra uma venda e remove o veículo vendido do estoque.
     * @throws VeiculoNaoEncontradoException se o chassi não existir no estoque
     */
    public Venda registrarVenda(String chassi, Cliente cliente, Vendedor vendedor)
            throws VeiculoNaoEncontradoException {
        Veiculo veiculo = veiculoRepository.buscarPorChassi(chassi);
        Venda venda = new Venda(proximoId++, veiculo, cliente, vendedor, LocalDate.now());
        veiculoRepository.remover(chassi);
        vendas.add(venda);
        return venda;
    }

    /** Retorna uma lista não modificável das vendas desta execução. */
    public List<Venda> listarVendas() {
        return Collections.unmodifiableList(new ArrayList<>(vendas));
    }

    /** Soma os valores finais das vendas no período informado, incluindo as datas-limite.
     * @throws IllegalArgumentException se as datas forem nulas ou o período estiver invertido
     */
    public double totalVendidoNoPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null || fim.isBefore(inicio)) {
            throw new DadosInvalidosException("Informe um período válido: a data final não pode ser anterior à inicial.");
        }
        return vendas.stream()
                .filter(venda -> !venda.getDataVenda().isBefore(inicio)
                        && !venda.getDataVenda().isAfter(fim))
                .mapToDouble(Venda::getValorFinal)
                .sum();
    }
}
