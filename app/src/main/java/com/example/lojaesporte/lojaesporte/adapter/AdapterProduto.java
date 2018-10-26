package com.example.lojaesporte.lojaesporte.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import com.example.lojaesporte.lojaesporte.R;
import com.example.lojaesporte.lojaesporte.helper.RecyclerItemClickListener;
import com.example.lojaesporte.lojaesporte.model.Produto;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jamilton Damasceno
 */

public class AdapterProduto extends RecyclerView.Adapter<AdapterProduto.MyViewHolder>{

    List<Produto> produtos;
    Context context;
    List<CardView>cardViewList = new ArrayList<>();

    public AdapterProduto(List<Produto> produtos, Context context) {
        this.produtos = produtos;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_produto, parent, false);
        return new MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Produto produto = produtos.get(position);

        holder.descricao.setText(produto.getDescricao());
        holder.preco.setText(String.valueOf(produto.getPreco()));
        holder.categoria.setText(produto.getCategoria());
        holder.quantidade.setText(String.valueOf(produto.getQuantidade()));

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

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(CardView cardView : cardViewList){
                    cardView.setCardBackgroundColor(Color.WHITE);
                }
                //The selected card is set to colorSelected
                holder.cardView.setCardBackgroundColor(Color.WHITE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView descricao, preco, categoria, quantidade;
        CardView cardView;
        RecyclerView recyclerView;
        public MyViewHolder(View itemView) {
            super(itemView);

            descricao = itemView.findViewById(R.id.textAdapterDescricao);
            preco = itemView.findViewById(R.id.textAdapterPreco);
            categoria = itemView.findViewById(R.id.textAdapterCategoria);
            quantidade = itemView.findViewById(R.id.textAdapterQuantidade);
            cardView = itemView.findViewById(R.id.cardViewProduto);
            recyclerView = itemView.findViewById(R.id.recycleView);
        }

    }

}
