package com.example.lojaesporte.lojaesporte.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lojaesporte.lojaesporte.DAO.ProdutoDAO;
import com.example.lojaesporte.lojaesporte.DAO.VendaDAO;
import com.example.lojaesporte.lojaesporte.R;
import com.example.lojaesporte.lojaesporte.helper.Utils;
import com.example.lojaesporte.lojaesporte.services.VendaService;
import com.example.lojaesporte.lojaesporte.adapter.AdapterProdutoVenda;
import com.example.lojaesporte.lojaesporte.config.ConfiguracaoFirebase;
import com.example.lojaesporte.lojaesporte.helper.DateCustom;
import com.example.lojaesporte.lojaesporte.helper.MaskEditUtil;
import com.example.lojaesporte.lojaesporte.helper.RecyclerItemClickListener;
import com.example.lojaesporte.lojaesporte.model.ProdutoVenda;
import com.example.lojaesporte.lojaesporte.model.Venda;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class CaixaActivity extends AppCompatActivity {
    private List<ProdutoVenda> produtosVenda = new ArrayList<>();
    private ProdutoVenda produto;
    private ProdutoVenda produtoSelecionado;
    private static final int REQUEST_CODE = 0;
    private AdapterProdutoVenda adapterProdutoVenda;
    private RecyclerView recyclerView;
    private EditText textValorTotal,textData,textCliente;
    private Spinner spinner;
    private VendaDAO vendaDAO = new VendaDAO();
    private VendaService vendaService = new VendaService();
    private Venda venda = new Venda();
    private DatabaseReference firebaseDatabase;
    private ProdutoDAO produtoDAO = new ProdutoDAO();
    private int posicao;
    private Utils utils = new Utils();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caixa);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerViewCarrinho);
        textValorTotal = findViewById(R.id.editTValorTotalVenda);
        textData = findViewById(R.id.editTDataVenda);
        textCliente = findViewById(R.id.editTClienteVenda);

        textData.setText(DateCustom.dataAtual());
        textValorTotal.setEnabled(false);
        textData.addTextChangedListener(MaskEditUtil.mask(textData, MaskEditUtil.FORMAT_DATE));

        firebaseDatabase = ConfiguracaoFirebase.getFirebaseDatabase();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListaProdutosActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });

        adapterProdutoVenda = new AdapterProdutoVenda(produtosVenda, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterProdutoVenda);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                                    produtoSelecionado = produtosVenda.get(position);
                                    posicao = position;
                                    //cardView.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                                    // recyclerView.
                                    deletaCarrinho();

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }));

        spinner =  findViewById(R.id.spinnerFormaPagemento);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterFormaPagemento = ArrayAdapter.createFromResource(this,
                R.array.formas_pagamento_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterFormaPagemento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapterFormaPagemento);
        spinner.setSelected(false);


    }

    public void adicionaProdutoCarrinho(ProdutoVenda produto){
        textValorTotal.setEnabled(true);
        produtosVenda.add(produto);
        adapterProdutoVenda.notifyDataSetChanged();
    }
    private void atribuiValorTotal(ProdutoVenda produto, Double valorTbx){
         textValorTotal.setText(String.valueOf(vendaDAO.calculaTotal(valorTbx,produto)));
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE){
            if(data != null){
                Bundle params = data.getExtras();
                if(resultCode == RESULT_OK){
                    if(params != null){
                        produto = (ProdutoVenda) params.getSerializable("produtoSelecionadoCarrinho");
                        if(vendaService.validaProdutoRepitido(produtosVenda,produto)){
                            adicionaProdutoCarrinho(produto);
                            if(textValorTotal.getText().toString().isEmpty()){
                                atribuiValorTotal(produto,Double.valueOf(0));
                            }else {
                                atribuiValorTotal(produto, Double.valueOf(textValorTotal.getText().toString()));
                            }
                        }else {
                            Toast.makeText(getApplicationContext(),"Este produto já foi adicionado no carrinho",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_caixa, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuFinilizar:
                if(utils.verificaConexao(getApplicationContext())){
                    insereVenda(produtosVenda,venda);
                }else {
                    Toast.makeText(getApplicationContext(),"Sem conexão com a internet!",Toast.LENGTH_SHORT).show();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void atualizaEstoque(List<ProdutoVenda> produtosVenda){
       for (ProdutoVenda produto : produtosVenda){
            produtoDAO.atualizarEstoque(produto);
       }

    }
    private void insereVenda(List<ProdutoVenda> produtosVenda, Venda venda){
        venda.setNomeCliente(textCliente.getText().toString());
        venda.setData(textData.getText().toString());
        venda.setFormaPagamento(spinner.getSelectedItem().toString());
        if(!textValorTotal.getText().toString().isEmpty()){
            venda.setValorTotal(Double.valueOf(textValorTotal.getText().toString()));
        }else {
            venda.setValorTotal(null);
            Toast.makeText(getApplicationContext(),"Valor total não pode estar vazio",Toast.LENGTH_SHORT);
        }

            if (vendaService.camposValidos(venda)) {
                if (vendaService.validaCarrinho(produtosVenda)) {
                    vendaService.inserir(venda, produtosVenda);
                    atualizaEstoque(produtosVenda);
                    limpaVenda();
                    Toast.makeText(getApplicationContext(), "Venda finalizada com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Necessário um item no carrinho!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Campos vazios ou invalidos!", Toast.LENGTH_SHORT).show();
            }
    }

    private void limpaVenda(){
        produtosVenda.clear();
        textCliente.setText("");
        textValorTotal.setText("0");
        spinner.setSelection(0);

        recyclerView.setAdapter(adapterProdutoVenda);
        adapterProdutoVenda.notifyDataSetChanged();
    }

    private void deletaCarrinho(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Confirmar exclusão");
        dialog.setMessage("Desaja excluir o produto: "+ produtosVenda.get(posicao).getDescricao() + "?");
        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    textValorTotal.setText(String.valueOf(vendaService.atualizaValorTotalExclusaoProdutoCarrinho(Double.valueOf(textValorTotal.getText().toString()),produtosVenda.get(posicao))));
                    produtosVenda.remove(posicao);
                    adapterProdutoVenda.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(),"Produto excluido do carrinho!", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setNegativeButton("Não", null);
        dialog.create();
        dialog.show();
    }
}
