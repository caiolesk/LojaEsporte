package com.example.lojaesporte.lojaesporte.activity;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.lojaesporte.lojaesporte.R;
import com.example.lojaesporte.lojaesporte.fragment.ListaDespesaFragment;
import com.example.lojaesporte.lojaesporte.fragment.ListaProdutoFragment;
import com.example.lojaesporte.lojaesporte.fragment.NovaDespesaFragment;
import com.example.lojaesporte.lojaesporte.fragment.NovoProdutoFragment;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class DespesaActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private SmartTabLayout smartTabLayout;
    private MaterialSearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesa);

        viewPager = findViewById(R.id.viewPager);
        smartTabLayout = findViewById(R.id.viewPagerTab);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Despesas");
        setSupportActionBar(toolbar);

        final FragmentPagerAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add("Nova Despesa", NovaDespesaFragment.class)
                        .add("Lista Despesas", ListaDespesaFragment.class)
                        .create()
        );

        viewPager.setAdapter(adapter);
        smartTabLayout.setViewPager(viewPager);

        viewPager.getCurrentItem();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        searchView = findViewById(R.id.materialSearchPrincipal);
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
