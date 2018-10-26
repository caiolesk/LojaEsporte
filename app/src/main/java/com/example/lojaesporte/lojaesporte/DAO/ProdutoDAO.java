package com.example.lojaesporte.lojaesporte.DAO;

import com.example.lojaesporte.lojaesporte.config.ConfiguracaoFirebase;
import com.example.lojaesporte.lojaesporte.model.Produto;
import com.example.lojaesporte.lojaesporte.model.ProdutoVenda;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class ProdutoDAO {
    private DatabaseReference firebase;
    public void inserir(Produto produto){
        firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("produto")
                .push()
                .setValue(produto);
    }
    public void excluir(Produto produto){
        firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("produto")
                .child(produto.getKey())
                .removeValue();
    }

    public void atualizarProduto(Produto produto){
        firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference produtoRef = firebase.child("produto")
                .child(produto.getKey());

        Map<String, Object> valoresProduto = converterParaMap(produto);

        produtoRef.updateChildren(valoresProduto);
    }

    @Exclude
    public Map<String, Object> converterParaMap(Produto produto){
        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("descricao",produto.getDescricao());
        usuarioMap.put("categoria",produto.getCategoria());
        usuarioMap.put("quantidade",produto.getQuantidade());
        usuarioMap.put("preco",produto.getPreco());

        return usuarioMap;
    }
    @Exclude
    public Map<String, Object> converterProdutoVendaParaMap(ProdutoVenda produto){
        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("quantidade", produto.getQuantidade());

        return usuarioMap;
    }

    public void atualizarEstoque(ProdutoVenda produtoVenda){
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();

        produtoVenda.setQuantidade(calculaAtualizacaoQtdEstoque(produtoVenda));

        Map<String, Object> valoresProduto = converterProdutoVendaParaMap(produtoVenda);

            DatabaseReference produtoRef = database.child("produto")
                    .child(produtoVenda.getKey());

            produtoRef.updateChildren(valoresProduto);
    }

    public int calculaAtualizacaoQtdEstoque(ProdutoVenda produtoVenda){
        return  produtoVenda.getQtdNoEstoque() - produtoVenda.getQuantidade();
    }
    public boolean validaQuantidade(ProdutoVenda produtoVenda){
        return produtoVenda.getQuantidade() <= produtoVenda.getQtdNoEstoque() && !(produtoVenda.getQuantidade() == 0);
    }

}
