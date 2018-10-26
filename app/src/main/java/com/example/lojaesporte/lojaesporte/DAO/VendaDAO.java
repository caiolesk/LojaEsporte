package com.example.lojaesporte.lojaesporte.DAO;

import com.example.lojaesporte.lojaesporte.config.ConfiguracaoFirebase;
import com.example.lojaesporte.lojaesporte.helper.DateCustom;
import com.example.lojaesporte.lojaesporte.model.Produto;
import com.example.lojaesporte.lojaesporte.model.ProdutoVenda;
import com.example.lojaesporte.lojaesporte.model.Venda;
import com.google.firebase.database.DatabaseReference;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class VendaDAO {

    public void inserir(Venda venda, List<ProdutoVenda> produtosVenda){
        DatabaseReference firebaseVenda = ConfiguracaoFirebase.getFirebaseDatabase();
        venda.setKey(String.valueOf(firebaseVenda.push().getKey().toString()));
        firebaseVenda.child("venda")
                .child(DateCustom.mesAnoDataEscolhida(venda.getData()))
                .child(venda.getKey())
                .setValue(venda);

        DatabaseReference firebaseProdutoVenda = ConfiguracaoFirebase.getFirebaseDatabase();
        for (Produto produtoVenda: produtosVenda){
            firebaseProdutoVenda.child("produtoVenda")
                    .child(DateCustom.mesAnoDataEscolhida(venda.getData()))
                    .child(venda.getKey())
                    .child(produtoVenda.getKey())
                    .setValue(produtoVenda);
        }
    }

    public double calculaTotal(Double valorAntigo,ProdutoVenda produtoVenda){
        return valorAntigo + ( produtoVenda.getPreco() * produtoVenda.getQuantidade() );
    }

    public double calculaPrecoDesconto(BigDecimal preco, BigDecimal desconto){
        //return preco - (preco * (desconto / 100));
       /* DecimalFormat df = new DecimalFormat("0.00");
        return Double.valueOf(df.format(preco - (preco * (desconto / 100))));
        */
       System.out.println(preco);
       preco = preco.subtract(preco.multiply(desconto.divide(new BigDecimal(100)))).setScale(2, RoundingMode.HALF_UP);
       System.out.println(preco);

       return preco.doubleValue();
    }
}
