package com.example.lojaesporte.lojaesporte.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lojaesporte.lojaesporte.DAO.ProdutoDAO;
import com.example.lojaesporte.lojaesporte.DAO.VendaDAO;
import com.example.lojaesporte.lojaesporte.R;
import com.example.lojaesporte.lojaesporte.model.Produto;
import com.example.lojaesporte.lojaesporte.model.ProdutoVenda;

import java.math.BigDecimal;

public class FinalizaSelecaoProdutoActivity extends AppCompatActivity {
    private  int posicao;
    private Produto produtoSelecionado = new Produto();
    private EditText textDescricao,textCategoria,textQuantidade,textPreco,textDesconto;
    private ProdutoVenda produtoVenda = new ProdutoVenda();
    private VendaDAO vendaDAO = new VendaDAO();
    private ProdutoDAO produtoDAO = new ProdutoDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finaliza_selecao_venda);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textDescricao = findViewById(R.id.editTDescricaoVenda);
        textCategoria = findViewById(R.id.editTCategoriaVenda);
        textQuantidade = findViewById(R.id.editTQuantidadeVenda);
        textPreco = findViewById(R.id.editTPrecoVenda);
        textDesconto = findViewById(R.id.editTDescontoVenda);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperaProduto();
    }

    @Override
    public void onBackPressed() {
        naoSelecionado();
        super.onBackPressed();
    }



    public void recuperaProduto(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            produtoSelecionado = (Produto) bundle.getSerializable("produtoSelecionado");
            textDescricao.setText(produtoSelecionado.getDescricao());
            textCategoria.setText(produtoSelecionado.getCategoria());
            textQuantidade.setText("1");
            textDesconto.setText("0");
            textPreco.setText(String.valueOf(produtoSelecionado.getPreco()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_seleciona, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSeleciona:
                    selecaoProduto();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void selecaoProduto(){
        Intent intent = new Intent();

            produtoVenda.setKey(produtoSelecionado.getKey());
            produtoVenda.setCategoria(textCategoria.getText().toString());
            produtoVenda.setDescricao(textDescricao.getText().toString());
            produtoVenda.setDesconto(Double.valueOf(textDesconto.getText().toString()));
            produtoVenda.setQuantidade(Integer.valueOf(textQuantidade.getText().toString()));
            produtoVenda.setQtdNoEstoque(produtoSelecionado.getQuantidade());

           // Double preco = vendaDAO.calculaPrecoDesconto(Double.valueOf(textPreco.getText().toString()),produtoVenda.getDesconto());

            produtoVenda.setPreco(vendaDAO.calculaPrecoDesconto(new BigDecimal(textPreco.getText().toString()),new BigDecimal(produtoVenda.getDesconto())));

            if(produtoDAO.validaQuantidade(produtoVenda)){
                intent.putExtra("produtoSelecionadoVenda",produtoVenda);

                setResult(1, intent);
                finish();
            }else {
                Toast.makeText(getApplicationContext(),"Quantidade invalida!",Toast.LENGTH_SHORT).show();
            }
    }

    private void naoSelecionado(){
        Intent intent = new Intent();
        setResult(2, intent);
        finish();
    }
}
