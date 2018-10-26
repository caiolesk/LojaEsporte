package com.example.lojaesporte.lojaesporte.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateCustom {
    public static String dataAtual(){
        long data = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataString = simpleDateFormat.format(data);

        return dataString;
    }
    public static String mesAnoDataEscolhida(String data){
        String retornoData[] = data.split("/");
        String mes = retornoData[1];
        String ano = retornoData[2];
        String mesAno = mes + ano;

        return mesAno;
    }
    public static boolean validaData(String data) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            sdf.parse(data);
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }
}
