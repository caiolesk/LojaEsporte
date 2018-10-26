package com.example.lojaesporte.lojaesporte.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lojaesporte.lojaesporte.R;
import com.example.lojaesporte.lojaesporte.model.Produto;
import com.example.lojaesporte.lojaesporte.model.Venda;

import java.util.ArrayList;
import java.util.List;

public class AdapterVenda extends RecyclerView.Adapter<AdapterVenda.MyViewHolder> {

    List<Venda> vendas;
    Context context;
    List<CardView>cardViewList = new ArrayList<>();

    public AdapterVenda(List<Venda> vendas, Context context) {
        this.vendas = vendas;
        this.context = context;
    }

    @Override
    public AdapterVenda.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_venda, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Venda venda = vendas.get(position);

        holder.nomeCliente.setText(venda.getNomeCliente());
        holder.valorTotal.setText(String.valueOf(venda.getValorTotal()));
        holder.formaPagamento.setText("Forma pag: " + venda.getFormaPagamento());
        holder.data.setText(String.valueOf(venda.getData()));

        cardViewList.add(holder.cardView);

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public boolean onLongClick(View v) {
                for(CardView cardView : cardViewList){
                    cardView.setCardBackgroundColor(Color.WHITE);
                }
                //The selected card is set to colorSelected
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.seletor_color));

                return true;
            }
        });

    }


    @Override
    public int getItemCount() {
        return vendas.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nomeCliente, valorTotal, formaPagamento, data;
        CardView cardView;
        RecyclerView recyclerView;
        public MyViewHolder(View itemView) {
            super(itemView);

            nomeCliente = itemView.findViewById(R.id.textAdapterNomeClienteVenda);
            valorTotal = itemView.findViewById(R.id.textAdapterValorTotalVenda);
            formaPagamento = itemView.findViewById(R.id.textAdapterFormaPagamentoVenda);
            data = itemView.findViewById(R.id.textAdapterDataVenda);
            cardView = itemView.findViewById(R.id.cardViewAdapterVenda);
            recyclerView = itemView.findViewById(R.id.recyclerViewVendas);
        }

    }
}
