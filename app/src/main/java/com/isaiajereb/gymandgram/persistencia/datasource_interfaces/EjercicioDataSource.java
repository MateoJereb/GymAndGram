package com.isaiajereb.gymandgram.persistencia.datasource_interfaces;

import com.isaiajereb.gymandgram.model.Ejercicio;
import com.isaiajereb.gymandgram.persistencia.OnResult;

import java.util.List;
import java.util.UUID;

public interface EjercicioDataSource {
    void guardarEjercicio(Ejercicio ejercicio, OnResult<Void> callback);

    void getEjercicios(UUID diaID, OnResult<List<Ejercicio>> callback);

    void editarEjercicio(Ejercicio ejercicio, OnResult<Void> callback);

    void eliminarEjercicio(Ejercicio ejercicio, OnResult<Void> callback);
}
