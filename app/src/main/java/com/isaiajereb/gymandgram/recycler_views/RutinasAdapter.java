package com.isaiajereb.gymandgram.recycler_views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.isaiajereb.gymandgram.R;
import com.isaiajereb.gymandgram.model.Rutina;

import java.util.List;

public class RutinasAdapter extends RecyclerView.Adapter<RutinasAdapter.RutinaViewHolder> {

    private List<Rutina> dataRutinas;
    public static class RutinaViewHolder extends RecyclerView.ViewHolder{
        LinearLayoutCompat card;
        TextView nombreRutinaTv;
        TextView rutinaActualTv;

        public RutinaViewHolder(@NonNull View itemView) {
            super(itemView);
            card= itemView.findViewById(R.id.fila_rutina);
            nombreRutinaTv= itemView.findViewById(R.id.nombreRutinaTextView);
            rutinaActualTv= itemView.findViewById(R.id.actualTextView);

        }
    }

    public RutinasAdapter(List<Rutina> dataRutinas){
        this.dataRutinas=dataRutinas;
    }

    @NonNull
    @Override
    public RutinasAdapter.RutinaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_rutina, parent, false);
        RutinaViewHolder rutinaVh = new RutinaViewHolder(v);
        return rutinaVh;
    }

    @Override
    public void onBindViewHolder(@NonNull RutinasAdapter.RutinaViewHolder holder, int position) {
        Rutina rutina = dataRutinas.get(position);
        holder.nombreRutinaTv.setText(rutina.getNombre());
        if(!rutina.getActual()){
            holder.rutinaActualTv.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return dataRutinas.size();
    }
}
