package com.isaiajereb.gymandgram.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;

import com.isaiajereb.gymandgram.R;
import com.isaiajereb.gymandgram.model.Ejercicio;
import com.isaiajereb.gymandgram.model.UnidadTiempo;

public class ConfigurarEjercicioDialog extends Dialog {

    private AppCompatImageView botonEliminar;
    private AppCompatEditText ejercicioEditText;
    private AppCompatEditText seriesEditText;
    private AppCompatEditText repeticionesEditText;
    private AppCompatEditText pesoEditText;
    private AppCompatEditText tiempoEditText;
    private AppCompatSpinner unidadTiempoSpinner;
    private AppCompatEditText observacionesEditText;
    private AppCompatButton confirmarButton;


    public ConfigurarEjercicioDialog(@NonNull Context context) {
        super(context);

        setContentView(R.layout.dialog_configurar_ejercicio);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().getAttributes().windowAnimations = R.style.animation;

        setCancelable(true);
        setCanceledOnTouchOutside(false);

        botonEliminar = findViewById(R.id.eliminarButton);
        ejercicioEditText = findViewById(R.id.ejercicioEditText);
        seriesEditText = findViewById(R.id.seriesEditText);
        repeticionesEditText = findViewById(R.id.repeticionesEditText);
        pesoEditText = findViewById(R.id.pesoEditText);
        tiempoEditText = findViewById(R.id.tiempoEditText);
        unidadTiempoSpinner = findViewById(R.id.unidadTiempoSpinner);
        observacionesEditText = findViewById(R.id.observacionesEditText);
        confirmarButton = findViewById(R.id.confirmarButton);

        //Llenar spiner de unidades de tiempo
        String[] unidadesTiempo = context.getResources().getStringArray(R.array.unidad_tiempo);
        ArrayAdapter<String> adapterUnidadTiempo = new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,unidadesTiempo);
        unidadTiempoSpinner.setAdapter(adapterUnidadTiempo);
    }

    public AppCompatImageView getBotonEliminar() {
        return botonEliminar;
    }

    public AppCompatEditText getEjercicioEditText() {
        return ejercicioEditText;
    }

    public AppCompatEditText getSeriesEditText() {
        return seriesEditText;
    }

    public AppCompatEditText getRepeticionesEditText() {
        return repeticionesEditText;
    }

    public AppCompatEditText getPesoEditText() {
        return pesoEditText;
    }

    public AppCompatEditText getTiempoEditText() {
        return tiempoEditText;
    }

    public AppCompatSpinner getUnidadTiempoSpinner() {
        return unidadTiempoSpinner;
    }

    public AppCompatEditText getObservacionesEditText() {
        return observacionesEditText;
    }

    public AppCompatButton getConfirmarButton() {
        return confirmarButton;
    }

    public void setDatosEjercicio(Ejercicio ejercicio){
        if(ejercicio.getNombre()!=null) ejercicioEditText.setText(ejercicio.getNombre());
        ejercicio.getSeries().ifPresent(s-> seriesEditText.setText(s.toString()));
        ejercicio.getRepeticiones().ifPresent(rep-> repeticionesEditText.setText(rep.toString()));
        ejercicio.getPeso().ifPresent(p-> pesoEditText.setText(p.toString()));
        ejercicio.getTiempo_cantidad().ifPresent(tc-> tiempoEditText.setText(tc.toString()));
        ejercicio.getTiempo_unidad().ifPresent(ut -> {
            if(ut.equals(UnidadTiempo.Minuto)) unidadTiempoSpinner.setSelection(0);
            else unidadTiempoSpinner.setSelection(1);
        });
        if(ejercicio.getObservaciones()!=null) observacionesEditText.setText(ejercicio.getObservaciones());


    }
}
