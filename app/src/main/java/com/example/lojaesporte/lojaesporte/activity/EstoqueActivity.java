package com.example.lojaesporte.lojaesporte.activity;

import android.content.Intent;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.lojaesporte.lojaesporte.R;
import com.example.lojaesporte.lojaesporte.fragment.ListaProdutoFragment;
import com.example.lojaesporte.lojaesporte.fragment.NovoProdutoFragment;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class EstoqueActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private SmartTabLayout smartTabLayout;
    public int habilitaMenu;
    private ListaProdutoFragment listaProdutoFragment;
    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estoque);

        viewPager = findViewById(R.id.viewPager);
        smartTabLayout = findViewById(R.id.viewPagerTab);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Estoque");
        setSupportActionBar(toolbar);

        final FragmentPagerAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add("Novo Produto", NovoProdutoFragment.class)
                        .add("Lista Produtos", ListaProdutoFragment.class)
                        .create()
        );

        viewPager.setAdapter(adapter);
        smartTabLayout.setViewPager(viewPager);

         viewPager.getCurrentItem();

         getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        searchView = findViewById(R.id.materialSearchPrincipal);
        /*
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                ListaProdutoFragment fragment = (ListaProdutoFragment) adapter.getItem(1);
                fragment.recarregarProdutos();
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ListaProdutoFragment fragment = (ListaProdutoFragment) adapter.getItem(1);
                if(newText != null && !newText.isEmpty()){
                    fragment.pesquisarConversas(newText.toLowerCase());
                }

                return true;
            }
        });
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        /*
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lista_produtos, menu);
        */
/*
        MenuItem item = menu.findItem(R.id.menuPesquisar);
        searchView.setMenuItem(item);
*/

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }
}
