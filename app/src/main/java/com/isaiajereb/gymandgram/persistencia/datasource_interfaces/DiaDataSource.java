package com.isaiajereb.gymandgram.persistencia.datasource_interfaces;

import com.isaiajereb.gymandgram.model.Dia;
import com.isaiajereb.gymandgram.persistencia.OnResult;

import java.util.List;
import java.util.UUID;

public interface DiaDataSource {
    void guardarDia(Dia dia, OnResult<Void> callback);

    void getDias(UUID semanaID, OnResult<List<Dia>> callback);

    void cambiarHora(Dia dia, OnResult<Void> callback);
}
