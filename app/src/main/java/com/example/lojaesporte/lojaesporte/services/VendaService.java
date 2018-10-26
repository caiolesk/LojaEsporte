package com.example.lojaesporte.lojaesporte.services;

import com.example.lojaesporte.lojaesporte.DAO.VendaDAO;
import com.example.lojaesporte.lojaesporte.helper.DateCustom;
import com.example.lojaesporte.lojaesporte.model.ProdutoVenda;
import com.example.lojaesporte.lojaesporte.model.Venda;

import java.util.List;

public class VendaService {
    private VendaDAO vendaDAO = new VendaDAO();

    public void inserir(Venda venda,List<ProdutoVenda> produtosVenda) {
        if(camposValidos(venda)){
            if(validaCarrinho(produtosVenda)){
                vendaDAO.inserir(venda,produtosVenda);
            }
        }
    }

    public boolean camposValidos(Venda venda) {

        if (venda.getData().equals("")) {
            return false;
        }
        if (!DateCustom.validaData(venda.getData())) {
            return false;
        }
        if (venda.getNomeCliente().equals("")) {
            return false;
        }
        if (!validaNome(venda.getNomeCliente())) {
            return false;
        }
        if ((venda.getFormaPagamento().equals(""))) {
            return false;
        }
        if ((venda.getValorTotal().equals(""))) {
            return false;
        }
        return true;
    }

    public boolean validaCarrinho(List<ProdutoVenda> produtosVenda) {
        return produtosVenda.size() > 0;
    }
    public boolean validaNome(String nome){
        return nome.matches("(?i:(([\\w+\\s+&&[^\\d]+&&[^\\p{Punct}]]+)))") & nome.length() > 3;
    }

    public double atualizaValorTotalExclusaoProdutoCarrinho(Double valorTotal, ProdutoVenda produtoVenda){
         return valorTotal - (produtoVenda.getPreco() * produtoVenda.getQuantidade());
    }
    public boolean validaSelecao(Venda venda){
        return venda.getKey() != null;
    }


}
