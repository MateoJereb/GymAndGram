package com.isaiajereb.gymandgram.recycler_views;

import android.content.Context;
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
    private LayoutInflater inflater;
    private Context context;
    private RutinasAdapter.OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(Rutina rutina);
    }
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

        void bindData(Rutina rutina, RutinasAdapter.OnItemClickListener listener){
            nombreRutinaTv.setText(rutina.getNombre());
            if(!rutina.getActual()){
                rutinaActualTv.setVisibility(View.INVISIBLE);
            }
            else{
                rutinaActualTv.setVisibility(View.VISIBLE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(rutina);
                }
            });
        }
    }

    public RutinasAdapter(List<Rutina> dataRutinas, Context context){
        this.dataRutinas=dataRutinas;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setDataRutinas(List<Rutina> dataRutinas) {
        this.dataRutinas = dataRutinas;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RutinasAdapter.RutinaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.fila_rutina, parent, false);
        RutinaViewHolder rutinaVh = new RutinaViewHolder(v);
        return rutinaVh;
    }

    @Override
    public void onBindViewHolder(@NonNull RutinasAdapter.RutinaViewHolder holder, int position) {
        holder.bindData(dataRutinas.get(position),listener);
    }

    @Override
    public int getItemCount() {
        return dataRutinas.size();
    }
}
