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

    public static final Integer RUTINA_GUARDADA = 1;
    public static final Integer SIN_ACTIVIDAD = 0;
    public static final Integer ERROR_AL_GUARDAR_RUTINA = -1;

    private MutableLiveData<List<Rutina>> rutinas;
    private MutableLiveData<Boolean> datosRutinaCargados;
    private List<Semana> semanas;
    private List<Dia> dias;
    private List<Ejercicio> ejercicios;
    public MutableLiveData<Integer> rutinaGuardada;
    private Rutina rutinaEnGuardado;

    private final RutinasRepository rutinasRepository;

    public RutinasViewModel(final RutinasRepository repository) {
        rutinas = new MutableLiveData<>(new ArrayList<>());
        semanas = new ArrayList<>();
        dias = new ArrayList<>();
        ejercicios = new ArrayList<>();

        datosRutinaCargados = new MutableLiveData<Boolean>(false);
        rutinaGuardada = new MutableLiveData<>(SIN_ACTIVIDAD);

        rutinasRepository = repository;

        buscarRutinas();
    }

    public void buscarRutinas(){
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

    //Metodo auxiliar para evitar actualizar nuevamente las listas al girar la pantalla
    public void notificarDatosRecibidos(){
        datosRutinaCargados.postValue(false);
    }

    public MutableLiveData<Integer> getRutinaGuardada() {
        return rutinaGuardada;
    }

    public void guardarRutinaCompleta(Rutina rutina, List<Semana> semanas, List<Dia> dias, List<Ejercicio> ejercicios){
        new Thread(() -> {
            rutinaEnGuardado = rutina;
            rutinasRepository.guardarRutinaCompleta(rutina,semanas,dias,ejercicios,rutinaGuardadaCallback);
        }).start();
    }

    public void editarRutinaCompleta(Rutina rutina, List<Semana> semanas, List<Dia> dias, List<Ejercicio> ejercicios){
        new Thread(() -> {
            rutinaEnGuardado = rutina;
            rutinasRepository.editarRutinaCompleta(rutina,semanas,dias,ejercicios,rutinaGuardadaCallback);
        }).start();
    }

    public void eliminarRutina(Rutina rutina){
        new Thread(() -> {
            rutinasRepository.eliminarRutina(rutina,rutinaEliminadaCallback);
        }).start();
    }

    public void notificarRutinaGuardadaRecibido(){
        rutinaGuardada.postValue(SIN_ACTIVIDAD);
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
            exception.printStackTrace();
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

    private OnResult<Void> rutinaGuardadaCallback = new OnResult<Void>(){
        @Override
        public void onSuccess(Void result) {
            rutinaGuardada.postValue(RUTINA_GUARDADA);
            Log.d("RutinasViewModel","Rutina guardada");

            buscarRutinas();
        }

        @Override
        public void onError(Throwable exception) {
            rutinaGuardada.postValue(ERROR_AL_GUARDAR_RUTINA);
            exception.printStackTrace();
            Log.e("RutinasViewModel","Error al guardar la rutina");
        }
    };

    private OnResult<Void> rutinaEliminadaCallback = new OnResult<Void>() {
        @Override
        public void onSuccess(Void result) {
            buscarRutinas();
        }

        @Override
        public void onError(Throwable exception) {
            Log.e("RutinasViewModel","Error al eliminar la rutina");
        }
    };
}
