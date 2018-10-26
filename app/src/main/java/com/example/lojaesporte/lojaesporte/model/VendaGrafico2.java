package com.example.lojaesporte.lojaesporte.model;

public class VendaGrafico2{
    public String mes;
    public double valor;


    public VendaGrafico2(String mes, double valor) {
        this.mes = mes;
        this.valor = valor;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}