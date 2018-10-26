package com.example.lojaesporte.lojaesporte.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lojaesporte.lojaesporte.R;
import com.example.lojaesporte.lojaesporte.services.ProdutoService;
import com.example.lojaesporte.lojaesporte.config.ConfiguracaoFirebase;
import com.example.lojaesporte.lojaesporte.model.Produto;
import com.google.firebase.database.DatabaseReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class NovoProdutoFragment extends Fragment {
    private Button btnCadastrar;
    private EditText textDescricao,textCategoria,textQuantidade,textPreco;
    private DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
    public NovoProdutoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_novo_produto, container, false);

        btnCadastrar = view.findViewById(R.id.btnCadastar);
        textDescricao = view.findViewById(R.id.editTDescricao);
        textCategoria = view.findViewById(R.id.editTCategoria);
        textQuantidade = view.findViewById(R.id.editTQuantidade);
        textPreco = view.findViewById(R.id.editTValorDespesa);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Produto produto = new Produto();
                ProdutoService produtoService = new ProdutoService();
                atribuiCampos(produto);

                if(produtoService.camposValidos(produto)){
                    produtoService.inserir(produto);
                    limpaCampos(produto);
                    Toast.makeText(getContext(),"Produto cadastrado com sucesso!",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(),"Preencher todos os campo!",Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    public void limpaCampos(Produto produto){
        produto = null;
        textDescricao.setText("");
        textPreco.setText("");
        textQuantidade.setText("");
        textCategoria.setText("");
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
