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
import com.example.lojaesporte.lojaesporte.services.VendaService;
import com.example.lojaesporte.lojaesporte.adapter.AdapterVenda;
import com.example.lojaesporte.lojaesporte.config.ConfiguracaoFirebase;
import com.example.lojaesporte.lojaesporte.helper.RecyclerItemClickListener;
import com.example.lojaesporte.lojaesporte.model.Venda;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.List;

public class ListaVendaActivity extends AppCompatActivity {
    private List<Venda> vendas = new ArrayList<>();
    private List<Venda> listaBuscaVendas = new ArrayList<>();
    private Venda vendaSelecionada = new Venda();
    private int posicao;
    private MaterialSearchView searchView;
    private MaterialCalendarView calendarView;
    private String mesAnoSelecionado;
    private DatabaseReference vendaRef;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private ValueEventListener valueEventListenerVenda;
    private RecyclerView recyclerViewVendas;
    private AdapterVenda adapterVenda;
    private VendaService vendaService = new VendaService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_venda);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Vendas");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchView = findViewById(R.id.materialSearchPrincipal);
        recyclerViewVendas = findViewById(R.id.recyclerViewVendas);
        adapterVenda = new AdapterVenda(vendas, getApplicationContext());
        calendarView = findViewById(R.id.calendarView);
        configuraCalendarView();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewVendas.setLayoutManager(layoutManager);
        recyclerViewVendas.setHasFixedSize(true);
        recyclerViewVendas.setAdapter(adapterVenda);

        recyclerViewVendas.addOnItemTouchListener(new RecyclerItemClickListener(
                getApplicationContext(),
                recyclerViewVendas,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                       // abreVenda();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                        if(!listaBuscaVendas.isEmpty()){
                            vendaSelecionada = listaBuscaVendas.get(position);
                            posicao = position;
                            //Toast.makeText(getApplicationContext(),"Venda selecionada!",Toast.LENGTH_SHORT).show();
                        }else {
                            vendaSelecionada = vendas.get(position);
                            posicao = position;
                            //Toast.makeText(getApplicationContext(), "Venda selecionada!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                recarregarVendas();
                listaBuscaVendas.clear();
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
                    pesquisarVendas(newText.toLowerCase());
                }
                return true;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarVendas();
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        vendaRef.removeEventListener(valueEventListenerVenda);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.clear();
        // TODO Add your menu entries here
        getMenuInflater().inflate(R.menu.menu_venda, menu);

        final MenuItem item = menu.findItem(R.id.menuPesquisarVenda);
        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAvancarPesquisa:
                   abreVenda();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void abreVenda(){

        if(vendaService.validaSelecao(vendaSelecionada)){
            Intent intent = new Intent(getApplicationContext(), VendaActivity.class);
            intent.putExtra("vendaSelecionada",vendaSelecionada);
            intent.putExtra("mesAno",mesAnoSelecionado);
            startActivity(intent);
            vendaSelecionada.setKey(null);
        }else{
            Toast.makeText(getApplicationContext(),"Venda não selecionado!",Toast.LENGTH_SHORT).show();
        }
    }
    public void recuperarVendas(){

        vendaRef = firebaseRef.child("venda").child(mesAnoSelecionado);
        valueEventListenerVenda = vendaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                vendas.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren()){

                    Venda venda = dados.getValue( Venda.class );
                    venda.setKey(dados.getKey());
                    vendas.add( venda );
                }
                adapterVenda.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void recarregarVendas(){
        adapterVenda = new AdapterVenda(vendas, getApplicationContext());
        recyclerViewVendas.setAdapter(adapterVenda);
        adapterVenda.notifyDataSetChanged();
    }

    public void pesquisarVendas(String texto){
        listaBuscaVendas.clear();
        List<Venda> lista = new ArrayList<>();
        for (Venda venda: vendas){
            String nomeCliente = venda.getNomeCliente().toLowerCase();
            if(nomeCliente.contains(texto)){
                lista.add(venda);
            }
        }

        adapterVenda = new AdapterVenda(lista, getApplicationContext());
        recyclerViewVendas.setAdapter(adapterVenda);
        adapterVenda.notifyDataSetChanged();
        listaBuscaVendas = lista;
    }
    public void configuraCalendarView(){
        CharSequence meses[] = {"Janeiro","Fevereiro","Março","Abril","Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"};

        calendarView.setTitleMonths(meses);
        CalendarDay dataAtual = calendarView.getCurrentDate();
        String mesSelecionado = String.format("%02d",(dataAtual.getMonth() + 1));
        mesAnoSelecionado = String.valueOf(mesSelecionado + "" + dataAtual.getYear());
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String mesSelecionado = String.format("%02d",(date.getMonth() + 1));
                mesAnoSelecionado = String.valueOf(mesSelecionado + "" + date.getYear());
                vendaRef.removeEventListener(valueEventListenerVenda);
                recuperarVendas();
            }
        });
    }

}
