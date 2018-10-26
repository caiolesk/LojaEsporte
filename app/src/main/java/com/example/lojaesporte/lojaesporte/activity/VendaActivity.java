package com.example.lojaesporte.lojaesporte.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.lojaesporte.lojaesporte.R;
import com.example.lojaesporte.lojaesporte.adapter.AdapterProduto;
import com.example.lojaesporte.lojaesporte.adapter.AdapterProdutoVenda;
import com.example.lojaesporte.lojaesporte.config.ConfiguracaoFirebase;
import com.example.lojaesporte.lojaesporte.helper.Utils;
import com.example.lojaesporte.lojaesporte.model.Produto;
import com.example.lojaesporte.lojaesporte.model.ProdutoVenda;
import com.example.lojaesporte.lojaesporte.model.Venda;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VendaActivity extends AppCompatActivity {
    private Venda venda;
    private EditText textValorTotal,textData,textCliente;
    private RecyclerView recyclerViewVendaSelecionada;
    private DatabaseReference produtoVendaRef;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private String mesAnoSelecionado;
    private ValueEventListener valueEventListenerProdutosVenda;
    private List<ProdutoVenda> produtosVenda = new ArrayList<>();
    private AdapterProdutoVenda adapterProdutoVenda;
    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venda);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Venda Selecionada");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerViewVendaSelecionada = findViewById(R.id.recyclerViewVendaSelecionada);
        textValorTotal = findViewById(R.id.editTValorTotalVendaSelecionada);
        textData = findViewById(R.id.editTDataVendaSelecionada);
        textCliente = findViewById(R.id.editTClienteVendaSelecionada);
        spinner =  findViewById(R.id.spinnerFormaPagementoVendaSelecionada);
        spinner.setEnabled(false);

        adapterProdutoVenda = new AdapterProdutoVenda(produtosVenda, getApplicationContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewVendaSelecionada.setLayoutManager(layoutManager);
        recyclerViewVendaSelecionada.setHasFixedSize(true);
        recyclerViewVendaSelecionada.setAdapter(adapterProdutoVenda);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterFormaPagemento = ArrayAdapter.createFromResource(this,
                R.array.formas_pagamento_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterFormaPagemento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapterFormaPagemento);
        spinner.setSelected(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperaVenda();
        recuperarProdutosVenda();
    }

    public void recuperaVenda(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            venda = (Venda) bundle.getSerializable("vendaSelecionada");
            mesAnoSelecionado = (String) bundle.getSerializable("mesAno");
            textCliente.setText(venda.getNomeCliente());
            textValorTotal.setText(String.valueOf(venda.getValorTotal()));
            textData.setText(String.valueOf(venda.getData()));
            spinner.setSelection(Utils.verificaValorSpinner(venda.getFormaPagamento()));
        }
    }

    public void recuperarProdutosVenda(){

        produtoVendaRef = firebaseRef.child("produtoVenda").child(mesAnoSelecionado).child(venda.getKey());
        valueEventListenerProdutosVenda = produtoVendaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                produtosVenda.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren()){

                    ProdutoVenda produtoVenda = dados.getValue( ProdutoVenda.class );
                    produtoVenda.setKey(dados.getKey());
                    produtosVenda.add( produtoVenda );
                }
                adapterProdutoVenda.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
