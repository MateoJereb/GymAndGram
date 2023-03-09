package com.isaiajereb.gymandgram.recycler_views;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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
    private RutinasAdapter.OnItemLongClickListener listenerLong;

    public interface OnItemClickListener{
        void onItemClick(Rutina rutina);
    }

    public interface OnItemLongClickListener{
        void onEditar(Rutina rutina);
        void onEliminar(Rutina rutina);
    }
    public static class RutinaViewHolder extends RecyclerView.ViewHolder {
        LinearLayoutCompat card;
        TextView nombreRutinaTv;
        TextView rutinaActualTv;

        public RutinaViewHolder(@NonNull View itemView) {
            super(itemView);
            card= itemView.findViewById(R.id.fila_rutina);
            nombreRutinaTv= itemView.findViewById(R.id.nombreRutinaTextView);
            rutinaActualTv= itemView.findViewById(R.id.actualTextView);
        }

        void bindData(Rutina rutina, RutinasAdapter.OnItemClickListener listener, RutinasAdapter.OnItemLongClickListener listenerLong){
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

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    SpannableString spannableEliminar = new SpannableString("Eliminar");
                    ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.parseColor("#FD6F6F"));
                    spannableEliminar.setSpan(redSpan,0,spannableEliminar.length(),SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

                    PopupMenu popup = new PopupMenu(itemView.getContext(),itemView, Gravity.END);
                    popup.getMenu().add("Editar").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(@NonNull MenuItem item) {
                            listenerLong.onEditar(rutina);
                            return true;
                        }
                    });

                    popup.getMenu().add(spannableEliminar).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(@NonNull MenuItem item) {
                            listenerLong.onEliminar(rutina);
                            return true;
                        }
                    });

                    popup.show();
                    return true;
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

    public void setOnItemLongClickListener(OnItemLongClickListener listenerLong) {
        this.listenerLong = listenerLong;
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
        holder.bindData(dataRutinas.get(position),listener,listenerLong);
    }

    @Override
    public int getItemCount() {
        return dataRutinas.size();
    }
}

