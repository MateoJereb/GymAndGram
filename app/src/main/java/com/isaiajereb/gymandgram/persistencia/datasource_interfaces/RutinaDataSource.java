package com.isaiajereb.gymandgram.persistencia.datasource_interfaces;

import com.isaiajereb.gymandgram.model.Dia;
import com.isaiajereb.gymandgram.model.Ejercicio;
import com.isaiajereb.gymandgram.model.Rutina;
import com.isaiajereb.gymandgram.model.Semana;
import com.isaiajereb.gymandgram.persistencia.OnResult;

import java.util.List;
import java.util.UUID;

public interface RutinaDataSource {
    void guardarRutina(Rutina rutina, OnResult<Void> callback);

    void guardarRutinaCompleta(Rutina rutina, List<Semana> semanas, List<Dia> dias, List<Ejercicio> ejercicios, OnResult<Void> callback);

    void editarRutinaCompleta(Rutina rutina, List<Semana> semanas, List<Dia> dias, List<Ejercicio> ejercicios, OnResult<Void> callback);

    void getRutinas(UUID userID, OnResult<List<Rutina>> callback);

    void getRutinaById(UUID rutinaID, OnResult<Rutina> callback);

    void eliminarRutina(Rutina rutina, OnResult<Void> callback);
}
