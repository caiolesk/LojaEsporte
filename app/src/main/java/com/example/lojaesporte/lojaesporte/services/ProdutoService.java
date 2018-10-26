package com.example.lojaesporte.lojaesporte.services;

import com.example.lojaesporte.lojaesporte.DAO.ProdutoDAO;
import com.example.lojaesporte.lojaesporte.model.Produto;

public class ProdutoService {
    private ProdutoDAO produtoDAO = new ProdutoDAO();

    public void inserir(Produto produto) {
        if(camposValidos(produto)){
            produtoDAO.inserir(produto);
        }
    }

    public boolean camposValidos(Produto produto) {

        if (produto.getDescricao().equals("")) {
            return false;
        }
            if (produto.getCategoria().equals("")) {
                return false;
            }
                if ((produto.getQuantidade() == 0)) {
                    return false;
                }
                    if ((produto.getPreco() == 0)) {
                        return false;
                    }

        return true;
    }
    public boolean atualizar(Produto produto){
        if(camposValidos(produto)){
            if(validaSelecao(produto)){
                produtoDAO.atualizarProduto(produto);
                return true;
            }
        }
        return false;
    }
    public boolean excluir(Produto produto){
       if(validaSelecao(produto)){
           produtoDAO.excluir(produto);
           return true;
       }else {
           return false;
       }
    }
    public boolean validaSelecao(Produto produto){
        return produto.getKey() != null;
    }


}
