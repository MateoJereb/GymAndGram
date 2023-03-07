package com.isaiajereb.gymandgram.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.isaiajereb.gymandgram.model.Dia;
import com.isaiajereb.gymandgram.model.Ejercicio;
import com.isaiajereb.gymandgram.model.Rutina;
import com.isaiajereb.gymandgram.repo.RutinasRepository;

import java.util.ArrayList;
import java.util.List;

public class RutinasViewModel extends AndroidViewModel {

    private MutableLiveData<List<Rutina>> rutinas;
    private MutableLiveData<List<Dia>> dias;
    private MutableLiveData<List<Ejercicio>> ejercicios;

    private final RutinasRepository rutinasRepository;

    public RutinasViewModel(@NonNull Application application) {
        super(application);

        rutinas = new MutableLiveData<>(new ArrayList<>());
        dias = new MutableLiveData<>(new ArrayList<>());
        ejercicios = new MutableLiveData<>(new ArrayList<>());

        rutinasRepository = new RutinasRepository();
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
}
