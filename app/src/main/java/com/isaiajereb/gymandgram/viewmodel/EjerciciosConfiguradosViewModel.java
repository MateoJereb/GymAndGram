package com.isaiajereb.gymandgram.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.isaiajereb.gymandgram.model.Ejercicio;

/*
    ViewModel utilizado para pasar el Ejercicio que se creó/editó
    desde el fragmento ConfigurarEjercicio al fragmento EditarRutina
 */
public class EjerciciosConfiguradosViewModel extends AndroidViewModel {
    private MutableLiveData<Ejercicio> ejercicioConfigurado;
    private MutableLiveData<Boolean> configurando;

    public EjerciciosConfiguradosViewModel(@NonNull Application application) {
        super(application);

        ejercicioConfigurado = new MutableLiveData<>();
        configurando = new MutableLiveData<>(false);
    }

    public MutableLiveData<Ejercicio> getEjercicioConfigurado() {
        return ejercicioConfigurado;
    }

    public void guardarEjercicio(Ejercicio ejercicio){
        ejercicioConfigurado.postValue(ejercicio);
    }

    public MutableLiveData<Boolean> getConfigurando() {
        return configurando;
    }

    public void comenzarConfiguracion(){ //Entra a la pantalla configurar ejercicio
        configurando.postValue(true);
    }

    public void finalizarConfiguracion(){ //Sale de la pantalla configurar ejercicio (dejar de observar ejercicioCreado/ejercicioEditado)
        configurando.postValue(false);
    }
}
