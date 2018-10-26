package com.example.lojaesporte.lojaesporte.services;

import com.example.lojaesporte.lojaesporte.DAO.DespesaDAO;
import com.example.lojaesporte.lojaesporte.helper.DateCustom;
import com.example.lojaesporte.lojaesporte.model.Despesa;

public class DespesaService {
    private DespesaDAO despesaDAO = new DespesaDAO();

    public void inserir(Despesa despesa) {
        if(camposValidos(despesa)){
            despesaDAO.inserir(despesa);
        }
    }

    public boolean camposValidos(Despesa despesa) {

        if (despesa.getData().equals("")) {
            return false;
        }
        if (!DateCustom.validaData(despesa.getData())) {
            return false;
        }
        if (despesa.getDescricao().equals("")) {
            return false;
        }
        if (despesa.getCategoria().equals("")) {
            return false;
        }
        if (despesa.getValor() == 0) {
            return false;
        }
        return true;
    }
}
