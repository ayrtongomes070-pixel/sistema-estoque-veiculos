package com.estoqueveiculos.model;

public class Caminhonete extends Veiculo {

    private double capacidadeCargaKg;

    public Caminhonete(String chassi, String modelo, String marca, int anoFabricacao,
                        double preco, double capacidadeCargaKg) {
        super(chassi, modelo, marca, anoFabricacao, preco);
        if (capacidadeCargaKg <= 0) {
            throw new IllegalArgumentException("Capacidade de carga deve ser maior que zero.");
        }
        this.capacidadeCargaKg = capacidadeCargaKg;
    }

    @Override
    public double calcularDesconto() {
        // Regra de exemplo: 3% de desconto para caminhonetes
        return getPreco() * 0.03;
    }

    public double getCapacidadeCargaKg() {
        return capacidadeCargaKg;
    }

    public void setCapacidadeCargaKg(double capacidadeCargaKg) {
        this.capacidadeCargaKg = capacidadeCargaKg;
    }

    @Override
    public String toString() {
        return super.toString() + " - Carga: " + capacidadeCargaKg + "kg";
    }
}
