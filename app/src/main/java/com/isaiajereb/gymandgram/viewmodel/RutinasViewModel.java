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
import com.isaiajereb.gymandgram.model.Usuario;
import com.isaiajereb.gymandgram.persistencia.OnResult;
import com.isaiajereb.gymandgram.repo.RutinasRepository;

import java.util.ArrayList;
import java.util.List;

public class RutinasViewModel extends ViewModel {

    private MutableLiveData<List<Rutina>> rutinas;
    private MutableLiveData<List<Dia>> dias;
    private MutableLiveData<List<Ejercicio>> ejercicios;

    private final RutinasRepository rutinasRepository;

    public RutinasViewModel(final RutinasRepository repository) {
        rutinas = new MutableLiveData<>(new ArrayList<>());
        dias = new MutableLiveData<>(new ArrayList<>());
        ejercicios = new MutableLiveData<>(new ArrayList<>());

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

    public MutableLiveData<List<Dia>> getDias() {
        return dias;
    }

    public MutableLiveData<List<Ejercicio>> getEjercicios() {
        return ejercicios;
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
}
