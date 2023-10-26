package com.isaiajereb.gymandgram.persistencia.datasource_interfaces;

import com.isaiajereb.gymandgram.model.Ejercicio;
import com.isaiajereb.gymandgram.persistencia.OnResult;

import java.util.List;
import java.util.UUID;

public interface EjercicioDataSource {
    void guardarEjercicio(Ejercicio ejercicio, OnResult<Void> callback);

    void guardarEjercicios(List<Ejercicio> ejercicios, OnResult<Void> callback);

    void getEjercicios(List<UUID> diasIDs, OnResult<List<Ejercicio>> callback);

    void editarEjercicio(Ejercicio ejercicio, OnResult<Void> callback);

    void eliminarEjercicio(Ejercicio ejercicio, OnResult<Void> callback);

    void eliminarEjercicios(List<Ejercicio> ejercicios, OnResult<Void> callback);
}
