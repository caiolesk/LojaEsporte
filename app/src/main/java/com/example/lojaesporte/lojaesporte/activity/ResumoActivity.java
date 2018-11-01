package com.example.lojaesporte.lojaesporte.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lojaesporte.lojaesporte.DAO.ResumoDAO;
import com.example.lojaesporte.lojaesporte.R;
import com.example.lojaesporte.lojaesporte.config.ConfiguracaoFirebase;
import com.example.lojaesporte.lojaesporte.helper.Datas;
import com.example.lojaesporte.lojaesporte.helper.DateCustom;
import com.example.lojaesporte.lojaesporte.helper.Utils;
import com.example.lojaesporte.lojaesporte.model.Produto;
import com.example.lojaesporte.lojaesporte.model.ProdutoVenda;
import com.example.lojaesporte.lojaesporte.model.Venda;
import com.example.lojaesporte.lojaesporte.model.VendaGrafico2;
import com.example.lojaesporte.lojaesporte.services.ResumoService;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ResumoActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private PieChart chart;
    private Spinner spinnerMes,spinnerAno,spinnerProcurar;
    private String dataAtual;
    private DatabaseReference vendaRef;
    private DatabaseReference produtoVendaRef;
    private DatabaseReference firebaseRef;
    private String mesAnoSelecionado;
    private ValueEventListener valueEventListenerVenda;
    private ValueEventListener valueEventListenerProdutosVenda;
    private List<Venda> vendasMensal = new ArrayList<>();
    private List<Venda> vendasMensalAno = new ArrayList<>();
    private List<Venda> vendasHoje = new ArrayList<>();
    private List<ProdutoVenda> produtosVendasHoje = new ArrayList<>();
    private List<ProdutoVenda> produtosVendasMensal = new ArrayList<>();
    private Button btnGerar,btnTst;
    private ResumoService resumoService = new ResumoService();
    private List<ProdutoVenda> produtosVendidoHojeGrafico = new ArrayList<>();
    private TextView textoTituloGrafico;
    private ResumoDAO resumoDAO = new ResumoDAO();
    private int posicaoNavegationBotton;
    private List<PieEntry> entries = new ArrayList<>();
    private List<VendaGrafico2> vendasAnualGraficos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumo);

        mTextMessage = findViewById(R.id.message);
        chart = findViewById(R.id.chart);
        spinnerMes =  findViewById(R.id.spinnerMes);
        spinnerAno = findViewById(R.id.spinnerAno);
        spinnerProcurar = findViewById(R.id.spinnerProcurarTipo);
        textoTituloGrafico = findViewById(R.id.textViewTitulo);

        dataAtual = DateCustom.dataAtual();
        firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Resumos");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        configuraBottomNavigationView();

        atribuiMesesSpinner();
        atribuiAnosSpinner();
        atribuiProcurarResumoSpinner();

        btnGerar = findViewById(R.id.btnGerar);
        btnGerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   switch (posicaoNavegationBotton){
                       case 1:
                           recuperarVendas();
                           setResumoHojeVenda();
                           break;
                       case 2:
                           setResumoMensalVenda();

                           break;
                       case 3:
                           configuraGraficoVendaAnual(entries);
                           break;
                   }

            }
        });

    }

    private void recuperaProdutosMensal(){
        produtosVendasMensal = resumoDAO.recuperarProdutosVendaMensal(vendasMensal,mesAnoSelecionado);
    }


    private void configuraBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigation);
        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableItemShiftingMode(true);
        bottomNavigationViewEx.enableShiftingMode(true);
        bottomNavigationViewEx.setTextVisibility(true);

        habilitarNavegacao(bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        posicaoNavegationBotton = 1;

    }

    private void habilitarNavegacao(BottomNavigationViewEx viewEx){
        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_dia:
                        posicaoNavegationBotton = 1;
                        if(spinnerProcurar.getSelectedItem().toString().equals("Vendas")){
                            recuperarVendas();
                        }else {

                        }

                        return true;
                    case R.id.ic_mes:
                        posicaoNavegationBotton = 2;
                        if(spinnerProcurar.getSelectedItem().toString().equals("Vendas")){
                            mesAnoSelecionado = Utils.getMesAtualSpinner(spinnerMes.getSelectedItem().toString()) + spinnerAno.getSelectedItem().toString();
                            vendasMensal.addAll( resumoDAO.recuperarVendasMensal(mesAnoSelecionado));
                            recuperaProdutosMensal();

                        }else {

                        }
                        return true;
                    case R.id.ic_ano:
                        posicaoNavegationBotton = 3;
                        if(spinnerProcurar.getSelectedItem().toString().equals("Vendas")){
                            String ano = spinnerAno.getSelectedItem().toString();
                            String meses[] = {"01","02","03","04","05","06","07","08","09","10","11","12"};
                            List<String> mesAnos = new ArrayList<>();
                            for(String mes : meses) {
                                String mesAno = mes + ano;

                                mesAnos.add(mesAno);
                            }

                            vendasAnualGraficos.clear();
                            recuperarVendasAnual(mesAnos);
                            }else {

                        }
                        return true;

                }
                return false;
            }
        });
    }
    public void getEntriesAnual(List<Venda> vendasM,String mes){

        if(vendasM.size() == 0){
            vendasAnualGraficos.add(new VendaGrafico2(Datas.getMesExtensoAnual(mes), 0));
        }else {
            double valorTotal = 0;
            for (Venda venda : vendasM) {
                valorTotal += venda.getValorTotal();

            }
            vendasAnualGraficos.add(new VendaGrafico2(Datas.getMesExtensoAnual(mes), valorTotal));
            entries = resumoService.getEntriesVendaAnual(vendasAnualGraficos);
        }
    }
    public void recuperarVendasAnual(List<String> mesesAno){
        List<Venda> vendas = new ArrayList<>();
        DatabaseReference vendaRef = ConfiguracaoFirebase.getFirebaseDatabase();

        for(final String mesAno : mesesAno){
            vendaRef.child("venda").child(mesAno).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    vendasMensalAno.clear();
                    for (DataSnapshot dados: dataSnapshot.getChildren()){

                        Venda venda = dados.getValue( Venda.class );
                        venda.setKey(dados.getKey());
                        vendasMensalAno.add( venda );
                    }
                    getEntriesAnual(vendasMensalAno,mesAno);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarVendas();
        setResumoHojeVenda();
    }

    public void recuperarProdutosVendaHoje(){
        vendasHoje = ResumoService.getVendasHoje(vendasMensal,dataAtual);
        produtosVendasHoje.clear();
        produtosVendidoHojeGrafico.clear();
            for (Venda v : vendasHoje) {
                produtoVendaRef = firebaseRef.child("produtoVenda").child(mesAnoSelecionado).child(v.getKey());
                produtoVendaRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dados : dataSnapshot.getChildren()) {
                            ProdutoVenda produtoVenda = dados.getValue(ProdutoVenda.class);
                            produtoVenda.setKey(dados.getKey());
                            produtosVendasHoje.add(produtoVenda);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
    }

    private boolean recuperarVendas(){
        mesAnoSelecionado = Utils.getMesAtualSpinner(spinnerMes.getSelectedItem().toString()) + spinnerAno.getSelectedItem().toString();
        vendaRef = firebaseRef.child("venda").child(mesAnoSelecionado);
        valueEventListenerVenda = vendaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                vendasMensal.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren()){

                    Venda venda = dados.getValue( Venda.class );
                    venda.setKey(dados.getKey());
                    vendasMensal.add( venda );
                }
                recuperarProdutosVendaHoje();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return true;
    }

    private void setResumoHojeVenda(){
        produtosVendidoHojeGrafico.clear();
        produtosVendidoHojeGrafico = resumoService.getProdutosGrafico(produtosVendasHoje);
        configuraGraficoVendaHoje(produtosVendidoHojeGrafico);
    }
    private void setResumoMensalVenda(){
        List<ProdutoVenda> produtoVendasMensalGrafico = new ArrayList<>();
        produtoVendasMensalGrafico = resumoService.getProdutosGrafico(produtosVendasMensal);
        configuraGraficoVendaMensal(produtoVendasMensalGrafico);
    }
    public void configuraGraficoVendaHoje(List<ProdutoVenda> produtos){
        textoTituloGrafico.setText("Quantidade produtos vendido hoje!");

        List<PieEntry> entries;

        entries = ResumoService.getEntriesProdutosGrafico(produtos);

        PieDataSet dataSet = new PieDataSet(entries, ""); // add entries to dataset
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextColor(R.color.colorPrimary);

        PieData pieData = new PieData(dataSet);
        chart.setData(pieData);
        chart.animateY(1000);
        chart.invalidate(); //
    }

    public void configuraGraficoVendaMensal(List<ProdutoVenda> produtos){
        textoTituloGrafico.setText("Quantidade produtos vendido mensal!");

        List<PieEntry> entries;

        entries = ResumoService.getEntriesProdutosGrafico(produtos);

        PieDataSet dataSet = new PieDataSet(entries, ""); // add entries to dataset
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextColor(R.color.colorPrimary);

        PieData pieData = new PieData(dataSet);
        chart.setData(pieData);
        chart.animateY(1000);
        chart.invalidate(); //
    }

    public void configuraGraficoVendaAnual(List<PieEntry> entries){
        textoTituloGrafico.setText("Valor da renda nos meses!");

        List<PieEntry> entriesPie = new ArrayList<>();
        for(PieEntry entry : entries){
            if(entry.getY() != 0){
                entriesPie.add(entry);
            }
        }

        PieDataSet dataSet = new PieDataSet(entriesPie, ""); // add entries to dataset
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextColor(R.color.colorPrimary);

        PieData pieData = new PieData(dataSet);
        chart.setData(pieData);
        chart.animateY(1000);
        chart.invalidate(); //
    }


    private void atribuiAnosSpinner(){
        List<String> anos = new ArrayList<>();

        anos = Utils.getAnosSpinner(dataAtual);

        Collections.sort(anos); //planetsArray will be sorted

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, anos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAno.setAdapter(adapter);
        spinnerAno.setSelection(4);
    }

    private void atribuiMesesSpinner(){
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterFormaPagemento = ArrayAdapter.createFromResource(this,
                R.array.meses_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterFormaPagemento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerMes.setAdapter(adapterFormaPagemento);
        spinnerMes.setSelected(false);
        int mesAtual = Datas.splitMonth(Datas.getDate(dataAtual));
        spinnerMes.setSelection(Utils.setMesAtualSpinner(mesAtual));
    }

    private void atribuiProcurarResumoSpinner(){
        ArrayAdapter<CharSequence> adapterFormaPagemento = ArrayAdapter.createFromResource(this,
                R.array.formas_procurar_resumo_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterFormaPagemento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerProcurar.setAdapter(adapterFormaPagemento);
        spinnerProcurar.setSelected(false);
    }
}
