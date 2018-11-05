package com.example.lojaesporte.lojaesporte.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.lojaesporte.lojaesporte.R;
import com.example.lojaesporte.lojaesporte.services.ProdutoService;
import com.example.lojaesporte.lojaesporte.adapter.AdapterProduto;
import com.example.lojaesporte.lojaesporte.config.ConfiguracaoFirebase;
import com.example.lojaesporte.lojaesporte.helper.RecyclerItemClickListener;
import com.example.lojaesporte.lojaesporte.model.Produto;
import com.example.lojaesporte.lojaesporte.model.ProdutoVenda;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class ListaProdutosActivity extends AppCompatActivity {
    private List<Produto> produtos = new ArrayList<>();
    private ProdutoVenda produto = null;
    private  int posicao;
    private Produto produtoSelecionado = new Produto();
    private Produto produtoSelecionadoVenda = new Produto();
    private RecyclerView recyclerView;
    private DatabaseReference produtoRef;
    private AdapterProduto adapterProduto;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private ValueEventListener valueEventListenerProdutos;
    private static final int REQUEST_CODE = 0;
    private ProdutoService produtoService = new ProdutoService();
    private MaterialSearchView searchView;
    private List<Produto> listaProdutosBusca = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Lista de Produtos");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        recyclerView = findViewById(R.id.recycleViewListaProdutos);

        adapterProduto = new AdapterProduto(produtos, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterProduto);

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
                                if(!listaProdutosBusca.isEmpty()){

                                    produtoSelecionado = listaProdutosBusca.get(position);
                                    posicao = position;
                                    //cardView.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                                    // recyclerView.
                                    //Toast.makeText(getApplicationContext(),"Produto selecionado " + produtoSelecionado.getDescricao(),Toast.LENGTH_SHORT).show();
                                }else {
                                    produtoSelecionado = produtos.get(position);
                                    posicao = position;
                                    //cardView.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                                    // recyclerView.
                                    //Toast.makeText(getApplicationContext(),"Produto selecionado " + produtoSelecionado.getDescricao(),Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

        searchView = findViewById(R.id.materialSearchPrincipal);

        //Listener para serchar view
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                recarregarProdutos();
                listaProdutosBusca.clear();
            }
        });

        //Listener caixa texto
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(newText != null && !newText.isEmpty()){
                    pesquisarProdutos(newText.toLowerCase());
                }

                return true;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarProdutos();
        selecaoProduto();
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    public void recuperarProdutos(){

        produtoRef = firebaseRef.child("produto");
        valueEventListenerProdutos = produtoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                produtos.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren()){

                    Produto produto = dados.getValue( Produto.class );
                    produto.setKey(dados.getKey());
                    produtos.add( produto );
                }
                adapterProduto.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void recarregarProdutos(){
        adapterProduto = new AdapterProduto(produtos, getApplicationContext());
        recyclerView.setAdapter(adapterProduto);
        adapterProduto.notifyDataSetChanged();
    }

    public void pesquisarProdutos(String texto){
        listaProdutosBusca.clear();
        List<Produto> lista = new ArrayList<>();
        for (Produto produto: produtos){
            String descricao = produto.getDescricao().toLowerCase();
            if(descricao.contains(texto)){
                lista.add(produto);
            }
        }
        adapterProduto = new AdapterProduto(lista, getApplicationContext());
        recyclerView.setAdapter(adapterProduto);
        adapterProduto.notifyDataSetChanged();
        listaProdutosBusca = lista;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE){
            Bundle params = data.getExtras();
            if(params != null){
                produto = (ProdutoVenda) params.getSerializable("produtoSelecionadoVenda");
            }else {
                produto = null;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_produtos_avancar, menu);

        MenuItem item = menu.findItem(R.id.menuPesquisarCarrinho);
        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAvancar:
                if(produtoService.validaSelecao(produtoSelecionado)){
                    abreTelaSelecao();
                }else {
                    Toast.makeText(getApplicationContext(),"Produto não selecionado!",Toast.LENGTH_SHORT).show();
                }
                break;
            case android.R.id.home:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        produtoRef.removeEventListener(valueEventListenerProdutos);
    }

    public void selecaoProduto(){
        if(produto != null){
            Intent intent = new Intent();
            intent.putExtra("produtoSelecionadoCarrinho",produto);

            setResult(-1, intent);
            produto = null;
            finish();
        }
    }

    public void abreTelaSelecao(){
        Intent intent = new Intent(getApplicationContext(), FinalizaSelecaoProdutoActivity.class);
        intent.putExtra("produtoSelecionado",produtoSelecionado);
        startActivityForResult(intent,REQUEST_CODE);
        produtoSelecionado.setKey(null);
    }
}
