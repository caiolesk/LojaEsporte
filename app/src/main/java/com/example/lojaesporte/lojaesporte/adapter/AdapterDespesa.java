package com.example.lojaesporte.lojaesporte.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lojaesporte.lojaesporte.R;
import com.example.lojaesporte.lojaesporte.model.Despesa;

import java.util.List;

public class AdapterDespesa extends RecyclerView.Adapter<AdapterDespesa.MyViewHolder> {

    List<Despesa> despesas;
    Context context;
    CardView cardView;

    public AdapterDespesa(List<Despesa> despesas, Context context) {
        this.despesas = despesas;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_despesa, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Despesa despesa = despesas.get(position);

        holder.descricao.setText(despesa.getDescricao());
        holder.valor.setText(String.valueOf(despesa.getValor()));
        holder.categoria.setText(despesa.getCategoria());
        holder.data.setText(String.valueOf(despesa.getData()));

        //holder.cardView.setBackgroundColor(Color.RED);
    }


    @Override
    public int getItemCount() {
        return despesas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView descricao, valor, categoria, data;

        RecyclerView recyclerView;
        public MyViewHolder(View itemView) {
            super(itemView);

            descricao = itemView.findViewById(R.id.textAdapterDescricaoDespesa);
            valor = itemView.findViewById(R.id.textAdapterValorDespesa);
            categoria = itemView.findViewById(R.id.textAdapterCategoriaDespesa);
            data = itemView.findViewById(R.id.textAdapterDataDespesa);
            cardView = itemView.findViewById(R.id.cardViewAdapterDespesa);
            recyclerView = itemView.findViewById(R.id.recycleViewDespesas);
        }

    }
}
