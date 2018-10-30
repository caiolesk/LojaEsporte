package com.example.lojaesporte.lojaesporte.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.lojaesporte.lojaesporte.R;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Utils {

    public static int verificaValorSpinner(String formaPagamento){
        //int numeroLista = R.array.formas_pagamento_array;
        if(formaPagamento.equals("Dinheiro")){
            return 1;
        }
        if(formaPagamento.equals("Crédito")){
            return 2;
        }
        if(formaPagamento.equals("Débito")){
            return 3;
        }
        if(formaPagamento.equals("Cheque")){
            return 4;
        }
        return 0;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork && activeNetwork.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public  boolean verificaConexao(Context context) {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }

    public static List<String> getAnosSpinner(String dataAtual){
        List<String> anos = new ArrayList<>();
        int anoAtual = Datas.splitYear(Datas.getDate(dataAtual));
        for(int x = 5; x >= 0; x--){
            String ano = String.valueOf(anoAtual - (x - 1));
            if(Integer.valueOf(ano) < anoAtual){
                anos.add(ano);
            }
        }
        anos.add(String.valueOf(anoAtual));

        return anos;
    }

    public static Integer setMesAtualSpinner(Integer mes) {
        if (mes != 0) {
            switch (mes) {
                case 01:
                    return 0;
                case 02:
                    return 1;
                case 03:
                    return 2;
                case 04:
                    return 3;
                case 05:
                    return 4;
                case 06:
                    return 5;
                case 07:
                    return 6;
                case 8:
                    return 7;
                case 9:
                    return 8;
                case 10:
                    return 9;
                case 11:
                    return 10;
                case 12:
                    return 11;
            }
        }
        return 0;
    }
    public static String getMesAtualSpinner(String mes) {
        if (mes != null) {
            switch (mes) {
                case "Jan":
                    return "01";
                case "Fev":
                    return "02";
                case "Mar":
                    return "03";
                case "Abr":
                    return "04";
                case "Mai":
                    return "05";
                case "Jun":
                    return "06";
                case "Jul":
                    return "07";
                case "Ago":
                    return "08";
                case "Set":
                    return "09";
                case "Out":
                    return "10";
                case "Nov":
                    return "11";
                case "Dez":
                    return "12";
            }
        }
        return null;
    }
}
