package com.example.lojaesporte.lojaesporte.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class ProdutoVenda extends Produto implements Serializable{
    private double desconto;
    private int qtdNoEstoque;

    public ProdutoVenda() {
    }
    public ProdutoVenda(String descricao, int qtd) {
        this.descricao = descricao;
        this.quantidade = qtd;
    }

    @Exclude
    public int getQtdNoEstoque() {
        return qtdNoEstoque;
    }

    public void setQtdNoEstoque(int qtdNoEstoque) {
        this.qtdNoEstoque = qtdNoEstoque;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }
}
