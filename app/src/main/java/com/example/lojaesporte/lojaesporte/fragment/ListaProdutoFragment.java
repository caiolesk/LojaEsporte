package com.example.lojaesporte.lojaesporte.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.lojaesporte.lojaesporte.DAO.ProdutoDAO;
import com.example.lojaesporte.lojaesporte.R;
import com.example.lojaesporte.lojaesporte.helper.Utils;
import com.example.lojaesporte.lojaesporte.services.ProdutoService;
import com.example.lojaesporte.lojaesporte.activity.EditarProdutoActivity;
import com.example.lojaesporte.lojaesporte.adapter.AdapterProduto;
import com.example.lojaesporte.lojaesporte.config.ConfiguracaoFirebase;
import com.example.lojaesporte.lojaesporte.helper.RecyclerItemClickListener;
import com.example.lojaesporte.lojaesporte.model.Produto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListaProdutoFragment extends Fragment {
    private ProdutoDAO produtoDAO = new ProdutoDAO();
    private CardView cardView;
    private List<Produto> produtos = new ArrayList<>();
    private Produto produto;
    private  int posicao;
    private Produto produtoSelecionado = new Produto();
    private DatabaseReference produtoRef;
    private AdapterProduto adapterProduto;
    private RecyclerView recyclerView;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private ValueEventListener valueEventListenerProdutos;
    private Context context;
    private MaterialSearchView searchView;
    private List<Produto> listaProdutosBusca = new ArrayList<>();
    private Utils utils = new Utils();
    public ListaProdutoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_produto, container, false);

        cardView = view.findViewById(R.id.cardView);
        recyclerView = view.findViewById(R.id.recycleView);
        adapterProduto = new AdapterProduto(produtos, getActivity());
        searchView = getActivity().findViewById(R.id.materialSearchPrincipal);

        setHasOptionsMenu(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterProduto);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
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
                }else {
                    produtoSelecionado = produtos.get(position);
                    posicao = position;

                }
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

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

        return view;
    }


    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        recuperarProdutos();

        super.onStart();

    }

    public void excluir(){
        ProdutoService produtoService = new ProdutoService();
        if(produtoService.excluir(produtoSelecionado)){
            adapterProduto.notifyItemRemoved(posicao);
            Toast.makeText(getContext(),"Produto excluido com sucesso!",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getContext(),"Produto não selecionado!",Toast.LENGTH_SHORT).show();
        }
        produtoSelecionado.setKey(null);
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
        adapterProduto = new AdapterProduto(produtos, getActivity());
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

        adapterProduto = new AdapterProduto(lista, getActivity());
        recyclerView.setAdapter(adapterProduto);
        adapterProduto.notifyDataSetChanged();
        listaProdutosBusca = lista;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        // TODO Add your menu entries here
        inflater.inflate(R.menu.menu_lista_produtos, menu);


        final MenuItem item = menu.findItem(R.id.menuPesquisar);
        searchView.setMenuItem(item);


        //super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuEditar:
                    ProdutoService produtoService = new ProdutoService();
                    if(produtoService.validaSelecao(produtoSelecionado)){
                        Intent intent = new Intent(getActivity(), EditarProdutoActivity.class);
                        intent.putExtra("produtoSelecionado",produtoSelecionado);
                        startActivity(intent);
                        produtoSelecionado.setKey(null);
                    }else{
                        Toast.makeText(getActivity(),"Produto não selecionado!",Toast.LENGTH_SHORT).show();
                    }
                break;

            case R.id.menuExcluir:

                excluir();

                break;
            case android.R.id.home:
                getActivity().finish();
                break;

            case R.id.menuPesquisar:


                break;
        }
        return true;

    }

    @Override
    public void onStop() {
        super.onStop();
        produtoRef.removeEventListener(valueEventListenerProdutos);
    }

}
