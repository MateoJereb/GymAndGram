package com.isaiajereb.gymandgram.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;

import com.isaiajereb.gymandgram.R;
import com.isaiajereb.gymandgram.model.Ejercicio;
import com.isaiajereb.gymandgram.model.UnidadTiempo;

import java.text.DecimalFormat;

public class ConfigurarEjercicioDialogFragment extends DialogFragment {

    private AppCompatImageView botonEliminar;
    private AppCompatEditText ejercicioEditText;
    private AppCompatEditText seriesEditText;
    private AppCompatEditText repeticionesEditText;
    private AppCompatEditText pesoEditText;
    private AppCompatEditText tiempoEditText;
    private AppCompatSpinner unidadTiempoSpinner;
    private AppCompatEditText observacionesEditText;
    private AppCompatTextView cancelarButton;
    private AppCompatTextView confirmarButton;

    private EliminarEjercicioListener listenerEliminar;
    private ConfirmarEjercicioListener listenerConfirmar;
    private CancelarListener listenerCancelar;

    public interface EliminarEjercicioListener{
        void onEliminar();
    }

    public interface ConfirmarEjercicioListener{
        void onConfirmar();
    }

    public interface CancelarListener{
        void onCancelar();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_configurar_ejercicio,null);

        botonEliminar = view.findViewById(R.id.eliminarButton);
        ejercicioEditText = view.findViewById(R.id.ejercicioEditText);
        seriesEditText = view.findViewById(R.id.seriesEditText);
        repeticionesEditText = view.findViewById(R.id.repeticionesEditText);
        pesoEditText = view.findViewById(R.id.pesoEditText);
        tiempoEditText = view.findViewById(R.id.tiempoEditText);
        unidadTiempoSpinner = view.findViewById(R.id.unidadTiempoSpinner);
        observacionesEditText = view.findViewById(R.id.observacionesEditText);
        cancelarButton = view.findViewById(R.id.cancelarButton);
        confirmarButton = view.findViewById(R.id.confirmarButton);

        //Llenar spiner de unidades de tiempo
        String[] unidadesTiempo = requireActivity().getResources().getStringArray(R.array.unidad_tiempo);
        ArrayAdapter<String> adapterUnidadTiempo = new ArrayAdapter<>(requireActivity(),android.R.layout.simple_list_item_1,unidadesTiempo);
        unidadTiempoSpinner.setAdapter(adapterUnidadTiempo);

        if(getArguments() != null){
            if(getArguments().get("ejercicio") != null){
                //Setear datos iniciales
                Ejercicio ejercicio = (Ejercicio) getArguments().get("ejercicio");

                if(ejercicio.getNombre()!=null) ejercicioEditText.setText(ejercicio.getNombre());
                ejercicio.getSeries().ifPresent(s-> seriesEditText.setText(s.toString()));
                ejercicio.getRepeticiones().ifPresent(rep-> repeticionesEditText.setText(rep.toString()));
                ejercicio.getPeso().ifPresent(p-> pesoEditText.setText(new DecimalFormat("#.##").format(p)));
                ejercicio.getTiempo_cantidad().ifPresent(tc-> tiempoEditText.setText(new DecimalFormat("#.##").format(tc)));
                ejercicio.getTiempo_unidad().ifPresent(ut -> {
                    if(ut.equals(UnidadTiempo.Minuto)) unidadTiempoSpinner.setSelection(0);
                    else unidadTiempoSpinner.setSelection(1);
                });
                if(ejercicio.getObservaciones()!=null) observacionesEditText.setText(ejercicio.getObservaciones());
            }
        }

        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerEliminar.onEliminar();
            }
        });

        cancelarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerCancelar.onCancelar();
            }
        });

        confirmarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ejercicioEditText.getText().toString().trim().isEmpty()){
                    new AlertDialog.Builder(requireContext())
                            .setMessage("Ingrese un nombre para el ejercicio")
                            .setPositiveButton("Aceptar",null)
                            .create()
                            .show();
                }
                else{
                    listenerConfirmar.onConfirmar();
                }
            }
        });

        return dialog;
    }

    public void setOnEliminarListener(EliminarEjercicioListener listenerEliminar) {
        this.listenerEliminar = listenerEliminar;
    }

    public void setOnCancelarListener(CancelarListener listenerCancelar){
        this.listenerCancelar = listenerCancelar;
    }

    public void setOnConfirmarListener(ConfirmarEjercicioListener listenerConfirmar) {
        this.listenerConfirmar = listenerConfirmar;
    }

    public String getEjercicioText(){
        return ejercicioEditText.getText().toString();
    }

    public String getSeriesText(){
        return seriesEditText.getText().toString();
    }

    public String getRepeticionesText(){
        return repeticionesEditText.getText().toString();
    }

    public String getPesoText(){
        return pesoEditText.getText().toString();
    }

    public String getTiempoText(){
        return tiempoEditText.getText().toString();
    }

    public Integer getUnidadTiempoPosition(){
        return unidadTiempoSpinner.getSelectedItemPosition();
    }

    public String getObervacionesText(){
        return observacionesEditText.getText().toString();
    }
}
