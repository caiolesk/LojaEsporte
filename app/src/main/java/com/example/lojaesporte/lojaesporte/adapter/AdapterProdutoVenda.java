package com.example.lojaesporte.lojaesporte.adapter;

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
import com.example.lojaesporte.lojaesporte.model.ProdutoVenda;

import java.util.List;

/**
 * Created by Jamilton Damasceno
 */

public class AdapterProdutoVenda extends RecyclerView.Adapter<AdapterProdutoVenda.MyViewHolder> implements View.OnLongClickListener {

    List<ProdutoVenda> produtos;
    Context context;
    CardView cardView;

    public AdapterProdutoVenda(List<ProdutoVenda> produtos, Context context) {
        this.produtos = produtos;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_produto_venda, parent, false);
        return new MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ProdutoVenda produto = produtos.get(position);

        holder.descricao.setText(produto.getDescricao());
        holder.preco.setText(String.valueOf(produto.getPreco()));
        holder.categoria.setText(produto.getCategoria());
        holder.quantidade.setText(String.valueOf(produto.getQuantidade()));
        holder.desconto.setText(String.valueOf(produto.getDesconto()));

    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    @Override
    public boolean onLongClick(View v) {

        cardView.setBackgroundColor(Color.RED);
        return true;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView descricao, preco, categoria, quantidade,desconto;

        RecyclerView recyclerView;
        public MyViewHolder(View itemView) {
            super(itemView);

            descricao = itemView.findViewById(R.id.textAdapterVendaDescricao);
            preco = itemView.findViewById(R.id.textAdapterVendaPreco);
            categoria = itemView.findViewById(R.id.textAdapterVendaCategoria);
            quantidade = itemView.findViewById(R.id.textAdapterVendaQuantidade);
            desconto = itemView.findViewById(R.id.textAdapterVendaDesconto);
            cardView = itemView.findViewById(R.id.cardView);
            recyclerView = itemView.findViewById(R.id.recycleView);
        }

    }

}
