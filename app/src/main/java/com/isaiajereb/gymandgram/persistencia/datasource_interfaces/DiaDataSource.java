package com.isaiajereb.gymandgram.persistencia.datasource_interfaces;

import com.isaiajereb.gymandgram.model.Dia;
import com.isaiajereb.gymandgram.persistencia.OnResult;

import java.util.List;
import java.util.UUID;

public interface DiaDataSource {
    void guardarDia(Dia dia, OnResult<Void> callback);

    void guardarDias(List<Dia> dias, OnResult<Void> callback);

    void getDias(List<UUID> semanasIDs, OnResult<List<Dia>> callback);

    void cambiarHora(Dia dia, OnResult<Void> callback);

    void eliminarDia(Dia dia, OnResult<Void> callback);

    void elmininarDias(List<Dia> dias, OnResult<Void> callback);

    void getDiasBySemanaID(UUID semanaID, OnResult<List<Dia>> callback);
}
