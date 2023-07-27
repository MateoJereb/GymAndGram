package com.isaiajereb.gymandgram.recycler_views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.isaiajereb.gymandgram.R;
import com.isaiajereb.gymandgram.model.Rutina;
import com.isaiajereb.gymandgram.model.Semana;

import java.util.List;

public class SemanasAdapter extends RecyclerView.Adapter<SemanasAdapter.SemanasViewHolder> {

    private List<Semana> dataSemanas;
    private LayoutInflater inflater;
    private Context context;
    private SemanasAdapter.OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(Semana semana);
    }

    public SemanasAdapter(List<Semana> listaSemanas, Context context){
        this.dataSemanas = listaSemanas;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setDataSemanas(List<Semana> dataSemanas){
        this.dataSemanas = dataSemanas;
    }

    public void setOnItemClickListener(SemanasAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public SemanasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fila_semana,parent,false);
        return new SemanasAdapter.SemanasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SemanasViewHolder holder, int position) {
        Boolean ultima = (position+1 == dataSemanas.size());
        holder.bindData(dataSemanas.get(position),listener,ultima);
    }

    @Override
    public int getItemCount() {
        return dataSemanas.size();
    }

    public static class SemanasViewHolder extends RecyclerView.ViewHolder {
        LinearLayoutCompat layout;
        AppCompatTextView nombre;
        LinearLayoutCompat separador;

        public SemanasViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreSemanaTextView);
            separador = itemView.findViewById(R.id.separadorSemanas);
        }

        void bindData(Semana semana, SemanasAdapter.OnItemClickListener listener, Boolean ultima){
            nombre.setText("Semana "+semana.getNumero().toString());

            if(ultima) separador.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(semana);
                }
            });
        }
    }
}
