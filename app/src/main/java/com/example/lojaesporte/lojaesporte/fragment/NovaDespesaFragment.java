package com.example.lojaesporte.lojaesporte.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lojaesporte.lojaesporte.R;
import com.example.lojaesporte.lojaesporte.helper.Utils;
import com.example.lojaesporte.lojaesporte.services.DespesaService;
import com.example.lojaesporte.lojaesporte.helper.DateCustom;
import com.example.lojaesporte.lojaesporte.helper.MaskEditUtil;
import com.example.lojaesporte.lojaesporte.model.Despesa;

/**
 * A simple {@link Fragment} subclass.
 */
public class NovaDespesaFragment extends Fragment {

    private Button btnCadastrarDespesa;
    private EditText textDescricao,textCategoria,textData,textValor;
    private TextWatcher textWatcher;
    private Utils utils = new Utils();
    public NovaDespesaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nova_despesa, container, false);

        btnCadastrarDespesa = view.findViewById(R.id.btnCadastrarDespesa);
        textDescricao = view.findViewById(R.id.editTDescricaoDespesa);
        textCategoria = view.findViewById(R.id.editTCategoriaDespesa);
        textData = view.findViewById(R.id.editTDataDespesa);
        textValor = view.findViewById(R.id.editTValorDespesa);

        textData.setText(DateCustom.dataAtual());
        textData.addTextChangedListener(MaskEditUtil.mask(textData, MaskEditUtil.FORMAT_DATE));


        btnCadastrarDespesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (utils.verificaConexao(getActivity())){
                    Despesa despesa = new Despesa();
                    DespesaService despesaService = new DespesaService();
                    atribuiCampos(despesa);

                    if(despesaService.camposValidos(despesa)){
                        despesaService.inserir(despesa);
                        limpaCampos(despesa);
                        Toast.makeText(getContext(),"Despesa cadastrada com sucesso!",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getContext(),"Preencher todos os campos!",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(),"Sem conexão com a internet!",Toast.LENGTH_SHORT).show();
                }


            }
        });
        return view;
    }

    public void limpaCampos(Despesa despesa){
        despesa = null;
        textDescricao.setText("");
        textValor.setText("");
        textCategoria.setText("");
    }
    public void atribuiCampos(Despesa despesa){
        despesa.setCategoria(textCategoria.getText().toString());
        despesa.setDescricao(textDescricao.getText().toString());
        despesa.setData(textData.getText().toString());

        if(textValor.getText().toString().isEmpty()){
            despesa.setValor((double) 0);
        }else {
            despesa.setValor(Double.valueOf(textValor.getText().toString()));
        }
    }
}
