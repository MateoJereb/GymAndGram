package com.isaiajereb.gymandgram.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.isaiajereb.gymandgram.model.Dia;
import com.isaiajereb.gymandgram.model.Ejercicio;
import com.isaiajereb.gymandgram.model.Rutina;
import com.isaiajereb.gymandgram.model.Semana;
import com.isaiajereb.gymandgram.model.Usuario;
import com.isaiajereb.gymandgram.persistencia.OnResult;
import com.isaiajereb.gymandgram.repo.RutinasRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class RutinasViewModel extends ViewModel {

    private MutableLiveData<List<Rutina>> rutinas;
    private MutableLiveData<Boolean> datosRutinaCargados;
    private List<Semana> semanas;
    private List<Dia> dias;
    private List<Ejercicio> ejercicios;

    private final RutinasRepository rutinasRepository;

    public RutinasViewModel(final RutinasRepository repository) {
        rutinas = new MutableLiveData<>(new ArrayList<>());
        semanas = new ArrayList<>();
        dias = new ArrayList<>();
        ejercicios = new ArrayList<>();

        datosRutinaCargados = new MutableLiveData<Boolean>(false);

        rutinasRepository = repository;

        //Recuperar rutinas de la BD
        new Thread(() -> {
            Log.d("RutinasViewModel","Buscar rutinas");
            rutinasRepository.recuperarRutinas(rutinasCargadasCallback);
        }).start();
    }

    public MutableLiveData<List<Rutina>> getRutinas() {
        return rutinas;
    }

    public List<Semana> getSemanas() { return semanas; }

    public List<Dia> getDias() {
        return dias;
    }

    public List<Ejercicio> getEjercicios() {
        return ejercicios;
    }

    public MutableLiveData<Boolean> getDatosRutinaCargados() {
        return datosRutinaCargados;
    }

    public void buscarDatosRutina(Rutina rutina){
        datosRutinaCargados.postValue(false);

        new Thread(() -> {
            Log.d("RutinasViewModel","Buscar datos rutina");
            rutinasRepository.recuperarDatosRutina(rutina,datosRutinaCallback);
        }).start();
    }

    private OnResult<List<Rutina>> rutinasCargadasCallback = new OnResult<List<Rutina>>() {
        @Override
        public void onSuccess(List<Rutina> result) {
            rutinas.postValue(result);
            Log.d("RutinasViewModel","Rutinas encontradas");
        }

        @Override
        public void onError(Throwable exception) {
            Log.e("RutinasViewModel","Error al buscar las rutinas");
        }
    };

    private OnResult<LinkedHashMap<String,Object>> datosRutinaCallback = new OnResult<LinkedHashMap<String, Object>>() {
        @Override
        public void onSuccess(LinkedHashMap<String, Object> result) {
            semanas = (List<Semana>) result.get("semanas");
            dias = (List<Dia>) result.get("dias");
            ejercicios = (List<Ejercicio>) result.get("ejercicios");

            datosRutinaCargados.postValue(true);
        }

        @Override
        public void onError(Throwable exception) {
            Log.e("RutinasViewModel","Error al buscar los datos de la rutina");
        }
    };
}
