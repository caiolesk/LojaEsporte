package com.example.lojaesporte.lojaesporte.DAO;

import com.example.lojaesporte.lojaesporte.config.ConfiguracaoFirebase;
import com.example.lojaesporte.lojaesporte.helper.DateCustom;
import com.example.lojaesporte.lojaesporte.model.Despesa;
import com.example.lojaesporte.lojaesporte.model.Produto;
import com.google.firebase.database.DatabaseReference;

public class DespesaDAO {

    private DatabaseReference firebase;
    public void inserir(Despesa despesa){
        firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        despesa.setKey(String.valueOf(firebase.push().getKey().toString()));
        firebase.child("despesa")
                .child(DateCustom.mesAnoDataEscolhida(despesa.getData()))
                .child(despesa.getKey())
                .setValue(despesa);
    }
}
