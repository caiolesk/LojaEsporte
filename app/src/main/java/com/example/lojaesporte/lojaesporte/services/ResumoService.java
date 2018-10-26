package com.example.lojaesporte.lojaesporte.services;

import com.example.lojaesporte.lojaesporte.DAO.ResumoDAO;
import com.example.lojaesporte.lojaesporte.helper.Datas;
import com.example.lojaesporte.lojaesporte.model.ProdutoVenda;
import com.example.lojaesporte.lojaesporte.model.Venda;
import com.example.lojaesporte.lojaesporte.model.VendaGrafico2;
import com.github.mikephil.charting.data.PieEntry;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ResumoService {

   public static List<Venda> getVendasHoje(List<Venda> vendas,String dataHoje){
       List<Venda> vendasHoje = new ArrayList<>();
       for(Venda v : vendas){
           if(v.getData().equals(dataHoje)){
               vendasHoje.add(v);
           }
       }
       return vendasHoje;
   }

   public List<ProdutoVenda> getProdutosGrafico(List<ProdutoVenda> produtoVendas){
       List<ProdutoVenda> resultado = new ArrayList<>();
       List<String> descricoes = new ArrayList<>();
       for (ProdutoVenda p : produtoVendas){
           if(!descricoes.contains(p.getDescricao())){
               descricoes.add(p.getDescricao());
           }
       }
       for (String d : descricoes){
           int qtd = 0;
           for (ProdutoVenda p : produtoVendas){
               if(p.getDescricao().equals(d)){
                   qtd += p.getQuantidade();
               }
           }
           resultado.add(new ProdutoVenda(d,qtd));
       }
       return resultado;
   }

   public static List<PieEntry> getEntriesProdutosGrafico(List<ProdutoVenda> produtoVendaHoje){
       List<PieEntry> entries = new ArrayList<>();
       for (ProdutoVenda p : produtoVendaHoje){
           entries.add(new PieEntry((float) p.getQuantidade(),p.getDescricao()));
       }
       return entries;
   }



   public List<PieEntry> getEntriesVendaAnual(List<VendaGrafico2> vendasMesesAnual){
       List<PieEntry> entries = new ArrayList<>();
       for (VendaGrafico2 vendaMes : vendasMesesAnual){
           entries.add(new PieEntry((float) vendaMes.getValor(),vendaMes.getMes()));
       }
       return entries;
   }

   /*
   List<Venda> vendasMensal = new ArrayList<>();
   public List<PieEntry> getVendasAnualGrafico(String anoSelecionado){
        ResumoDAO resumoDAO = new ResumoDAO();
        MesHelper mesHelper = new MesHelper();
        String meses[] = {"01","02","03","04","05","06","07","08","09","10","11","12"};
        List<VendaGrafico> vendasAnualGraficos = new ArrayList<>();
       // final List<Venda> vendasMensal = new ArrayList<>();
        for(String mes : meses){
            String mesAno = mes+anoSelecionado;

            vendasMensal.addAll( resumoDAO.recuperarVendasMensal(mesAno));


            if(vendasMensal.size() == 0){
                vendasAnualGraficos.add(new VendaGrafico(Datas.getMesExtenso(mes),0));
            }
            else {
                double valorTotal = 0;
                for (Venda venda : vendasMensal) {
                    valorTotal += venda.getValorTotal();

                }
                vendasAnualGraficos.add(new VendaGrafico(Datas.getMesExtenso(mes), valorTotal));
            }
            vendasMensal.clear();
        }
        List<PieEntry> entries = new ArrayList<>();
       // entries = getEntriesVendaAnual(vendasAnualGraficos);

        return entries;
   }



   public class VendaGrafico{
       public String mes;
       public double valor;


       public VendaGrafico(String mes, double valor) {
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



   public class MesHelper{
       public String JAN = "01";
       public String FEV = "02";
       public String MAR = "03";
       public String ABR = "04";
       public String MAI = "05";
       public String JUN = "06";
       public String JUL = "07";
       public String AGO = "08";
       public String SET = "09";
       public String OUT = "10";
       public String NOV = "11";
       public String DEZ = "12";

       public List<String> getMeses(){
         List<String> meses = new ArrayList<>();
         meses.add(JAN);
         meses.add(FEV);
         meses.add(MAR);
         meses.add(ABR);
         meses.add(MAI);
         meses.add(JUN);
         meses.add(JUL);
         meses.add(AGO);
         meses.add(SET);
         meses.add(OUT);
         meses.add(NOV);
         meses.add(DEZ);


         return meses;
       }
    }

    */
}
