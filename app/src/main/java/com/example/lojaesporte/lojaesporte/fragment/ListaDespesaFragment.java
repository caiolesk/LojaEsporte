package com.example.lojaesporte.lojaesporte.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import android.widget.CalendarView;

import com.example.lojaesporte.lojaesporte.R;
import com.example.lojaesporte.lojaesporte.adapter.AdapterDespesa;
import com.example.lojaesporte.lojaesporte.adapter.AdapterProduto;
import com.example.lojaesporte.lojaesporte.config.ConfiguracaoFirebase;
import com.example.lojaesporte.lojaesporte.helper.RecyclerItemClickListener;
import com.example.lojaesporte.lojaesporte.model.Despesa;
import com.example.lojaesporte.lojaesporte.model.Produto;
import com.google.firebase.auth.FirebaseAuth;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ListaDespesaFragment extends Fragment {
    private List<Despesa> despesas = new ArrayList<>();
    private List<Despesa> listaDespesasBusca = new ArrayList<>();
    private CardView cardView;
    private RecyclerView recyclerViewDespesas;
    private MaterialCalendarView calendarView;
    private int posicao;
    private Despesa despesaSelecionada = new Despesa();
    private MaterialSearchView searchView;
    private ValueEventListener valueEventListenerDespesas;
    private DatabaseReference despesaRef;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private AdapterDespesa adapterDespesa;
    private String mesAnoSelecionado;

    public ListaDespesaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_despesa, container, false);

        cardView = view.findViewById(R.id.cardView);
        recyclerViewDespesas = view.findViewById(R.id.recycleViewDespesas);
        adapterDespesa = new AdapterDespesa(despesas, getActivity());
        searchView = getActivity().findViewById(R.id.materialSearchPrincipal);
        calendarView = view.findViewById(R.id.calendarView);
        configuraCalendarView();
        setHasOptionsMenu(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewDespesas.setLayoutManager(layoutManager);
        recyclerViewDespesas.setHasFixedSize(true);
        recyclerViewDespesas.setAdapter(adapterDespesa);

        recyclerViewDespesas.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(),
                recyclerViewDespesas,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

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
                recarregarDespesas();
                listaDespesasBusca.clear();
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
                    pesquisarDespesas(newText.toLowerCase());
                }
                return true;
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        recuperarDespesas();
        super.onStart();
    }

    public void recuperarDespesas(){

        despesaRef = firebaseRef.child("despesa").child(mesAnoSelecionado);
        valueEventListenerDespesas = despesaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                despesas.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren()){

                    Despesa despesa = dados.getValue( Despesa.class );
                    despesa.setKey(dados.getKey());
                    despesas.add( despesa );
                }
                adapterDespesa.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void recarregarDespesas(){
        adapterDespesa = new AdapterDespesa(despesas, getActivity());
        recyclerViewDespesas.setAdapter(adapterDespesa);
        adapterDespesa.notifyDataSetChanged();
    }

    public void pesquisarDespesas(String texto){
        listaDespesasBusca.clear();
        List<Despesa> lista = new ArrayList<>();
        for (Despesa despesa: despesas){
            String descricao = despesa.getDescricao().toLowerCase();
            if(descricao.contains(texto)){
                lista.add(despesa);
            }
        }

        adapterDespesa = new AdapterDespesa(lista, getActivity());
        recyclerViewDespesas.setAdapter(adapterDespesa);
        adapterDespesa.notifyDataSetChanged();
        listaDespesasBusca = lista;
    }

    @Override
    public void onStop() {
        super.onStop();
        despesaRef.removeEventListener(valueEventListenerDespesas);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        // TODO Add your menu entries here
        inflater.inflate(R.menu.menu_despesa, menu);

        final MenuItem item = menu.findItem(R.id.menuPesquisarDespesa);
        searchView.setMenuItem(item);

        //super.onCreateOptionsMenu(menu, inflater);
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
                despesaRef.removeEventListener(valueEventListenerDespesas);
                recuperarDespesas();
            }
        });
    }
}
