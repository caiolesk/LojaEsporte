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

}
