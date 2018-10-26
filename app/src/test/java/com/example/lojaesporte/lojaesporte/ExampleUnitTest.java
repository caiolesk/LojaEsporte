package com.example.lojaesporte.lojaesporte;

import android.support.annotation.NonNull;

import com.example.lojaesporte.lojaesporte.config.ConfiguracaoFirebase;
import com.example.lojaesporte.lojaesporte.model.ProdutoVenda;
import com.example.lojaesporte.lojaesporte.services.ResumoService;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    //@Test
    public void testProdutos(){
        final List<ProdutoVenda> produtoVendas = new ArrayList();
        produtoVendas.add(new ProdutoVenda("Produto 1", 2));
        produtoVendas.add(new ProdutoVenda("Produto 1", 2));
        produtoVendas.add(new ProdutoVenda("Produto 2", 5));
        produtoVendas.add(new ProdutoVenda("Produto 2", 3));
        produtoVendas.add(new ProdutoVenda("Produto 3", 7));
        produtoVendas.add(new ProdutoVenda("Produto 3", 1));
        List<ProdutoVenda> produtoVendasHojeNRepitido = ResumoService.calculaQtdProdutoVendidosHoje(produtoVendas);
        for(ProdutoVenda p : produtoVendasHojeNRepitido){
            System.out.println("Descrição > " + p.getDescricao() + ", Quantidade > " + p.getQuantidade());
        }
        assertNotNull(produtoVendasHojeNRepitido);
    }

}