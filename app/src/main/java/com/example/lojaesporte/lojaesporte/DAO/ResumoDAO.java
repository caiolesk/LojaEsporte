package com.example.lojaesporte.lojaesporte.DAO;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.lojaesporte.lojaesporte.config.ConfiguracaoFirebase;
import com.example.lojaesporte.lojaesporte.helper.Utils;
import com.example.lojaesporte.lojaesporte.model.ProdutoVenda;
import com.example.lojaesporte.lojaesporte.model.Venda;
import com.example.lojaesporte.lojaesporte.services.ResumoService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ResumoDAO {
    DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();

    public List<Venda> recuperarVendasMensal(String mesAnoSelecionado){
        DatabaseReference vendaRef = ConfiguracaoFirebase.getFirebaseDatabase();
        final List<Venda> vendasMensal = new ArrayList<>();
        vendaRef.child("venda").child(mesAnoSelecionado).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                vendasMensal.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren()){

                    Venda venda = dados.getValue( Venda.class );
                    venda.setKey(dados.getKey());
                    vendasMensal.add( venda );
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return vendasMensal;
    }

    public List<ProdutoVenda> recuperarProdutosVendaMensal(List<Venda> vendasMensal,String mesAnoSelecionado){
        DatabaseReference produtoVendaRef;
        final List<ProdutoVenda> produtosVendasMensal = new ArrayList<>();
        for (Venda v : vendasMensal) {
            produtoVendaRef = firebaseRef.child("produtoVenda").child(mesAnoSelecionado).child(v.getKey());
            produtoVendaRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                        ProdutoVenda produtoVenda = dados.getValue(ProdutoVenda.class);
                        produtoVenda.setKey(dados.getKey());
                        produtosVendasMensal.add(produtoVenda);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        return produtosVendasMensal;
    }
}
