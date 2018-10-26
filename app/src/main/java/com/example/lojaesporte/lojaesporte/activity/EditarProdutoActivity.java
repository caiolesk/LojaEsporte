package com.example.lojaesporte.lojaesporte.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lojaesporte.lojaesporte.R;
import com.example.lojaesporte.lojaesporte.services.ProdutoService;
import com.example.lojaesporte.lojaesporte.model.Produto;

public class EditarProdutoActivity extends AppCompatActivity {
    private Produto produtoEditar;
    private EditText textDescricao,textCategoria,textQuantidade,textPreco;
    private Button btnSalvar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_produto);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Editar Produto");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnSalvar = findViewById(R.id.btnSalvar);
        textDescricao = findViewById(R.id.editTAlterarDescricao);
        textCategoria = findViewById(R.id.editTAlterarCategoria);
        textQuantidade = findViewById(R.id.editTAlterarQuantidade);
        textPreco = findViewById(R.id.editTAlterarPreco);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Produto produto = new Produto();
                produto.setKey(produtoEditar.getKey());
                ProdutoService produtoService = new ProdutoService();
                atribuiCampos(produto);

                if(produtoService.camposValidos(produto)){
                    produtoService.atualizar(produto);
                    Toast.makeText(getApplicationContext(),"Produto alterado com sucesso!",Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"Preencher todos os campo!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperaProduto();
    }

    public void recuperaProduto(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            produtoEditar = (Produto) bundle.getSerializable("produtoSelecionado");
            textDescricao.setText(produtoEditar.getDescricao());
            textCategoria.setText(produtoEditar.getCategoria());
            textQuantidade.setText(String.valueOf(produtoEditar.getQuantidade()));
            textPreco.setText(String.valueOf(produtoEditar.getPreco()));
        }
    }

    public void atribuiCampos(Produto produto){
        produto.setCategoria(textCategoria.getText().toString());
        produto.setDescricao(textDescricao.getText().toString());
        if(textQuantidade.getText().toString().isEmpty()){
            produto.setQuantidade(0);
        }else{
            produto.setQuantidade(Integer.valueOf(textQuantidade.getText().toString()));
        }
        if(textPreco.getText().toString().isEmpty()){
            produto.setPreco(0);
        }else {
            produto.setPreco(Double.valueOf(textPreco.getText().toString()));
        }
    }
}
