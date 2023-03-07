package com.isaiajereb.gymandgram.recycler_views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.isaiajereb.gymandgram.R;
import com.isaiajereb.gymandgram.model.Ejercicio;
import com.isaiajereb.gymandgram.model.UnidadTiempo;

import org.w3c.dom.Text;

import java.util.List;

public class EjerciciosAdapter extends RecyclerView.Adapter<EjerciciosAdapter.EjerciciosViewHolder> {
    private List<Ejercicio> listaEjercicios;
    private LayoutInflater inflater;
    private Context context;
    private EjerciciosAdapter.OnItemClickListener listener;

    public EjerciciosAdapter(List<Ejercicio> listaEjercicios, Context context){
        this.listaEjercicios = listaEjercicios;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setListaEjercicios(List<Ejercicio> listaEjercicios) {
        this.listaEjercicios = listaEjercicios;
    }

    public void setOnItemClickListener(EjerciciosAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public EjerciciosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fila_ejercicio,parent,false);
        return new EjerciciosAdapter.EjerciciosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EjerciciosViewHolder holder, int position) {
        holder.bindData(listaEjercicios.get(position),listener);
    }

    @Override
    public int getItemCount() {
        return listaEjercicios.size();
    }

    public interface OnItemClickListener{
        void onItemClick(Ejercicio item);
    }

    public static class EjerciciosViewHolder extends RecyclerView.ViewHolder{
        LinearLayoutCompat layout;
        AppCompatTextView nombre;
        AppCompatTextView detalles;

        public EjerciciosViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreEjercicioTextView);
            detalles = itemView.findViewById(R.id.detallesEjercicioTextView);
        }

        void bindData(Ejercicio ejercicio, EjerciciosAdapter.OnItemClickListener listener){
            nombre.setText(ejercicio.getNombre());

            String stringDetalles = "";
            Boolean primero = true, haySerie = false;

            if(ejercicio.getSeries().isPresent()){
                stringDetalles+= ejercicio.getSeries().get().toString();
                haySerie = true;
                primero = false;
            }

            if(ejercicio.getRepeticiones().isPresent()){
                if(haySerie) stringDetalles+= "x";
                stringDetalles+= ejercicio.getRepeticiones().get().toString();
                primero = false;
            }

            if(ejercicio.getPeso().isPresent()){
                if(!primero) stringDetalles+= " | ";
                stringDetalles+= ejercicio.getPeso().get().toString() + " kg";
                primero = false;
            }

            if(ejercicio.getTiempo_cantidad().isPresent()){
                if(!primero) stringDetalles+= " | ";
                stringDetalles+= ejercicio.getTiempo_cantidad().get().toString();

                if(ejercicio.getTiempo_unidad().get() == UnidadTiempo.Minuto)
                    stringDetalles+= " seg";
                else
                    stringDetalles+= " min";
            }

            detalles.setText(stringDetalles);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(ejercicio);
                }
            });
        }
    }
}
