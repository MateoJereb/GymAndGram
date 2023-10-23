package com.isaiajereb.gymandgram.persistencia.datasource_interfaces;

import com.isaiajereb.gymandgram.model.Semana;
import com.isaiajereb.gymandgram.persistencia.OnResult;
import com.isaiajereb.gymandgram.persistencia.room.entity.SemanaEntity;

import java.util.List;
import java.util.UUID;

public interface SemanaDataSource {
    void guardarSemana(Semana semana, OnResult<Void> callback);
    void guardarSemanas(List<Semana> semanas, OnResult<Void> callback);

    void getSemanas(UUID rutinaID, OnResult<List<Semana>> callback);

    void eliminarSemana(Semana semana, OnResult<Void> callback);
}
