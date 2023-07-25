package com.isaiajereb.gymandgram.persistencia.room.implementation;

import android.content.Context;

import com.isaiajereb.gymandgram.model.Dia;
import com.isaiajereb.gymandgram.persistencia.OnResult;
import com.isaiajereb.gymandgram.persistencia.datasource_interfaces.DiaDataSource;
import com.isaiajereb.gymandgram.persistencia.room.AppDatabase;
import com.isaiajereb.gymandgram.persistencia.room.dao.DiaDAO;
import com.isaiajereb.gymandgram.persistencia.room.entity.DiaEntity;
import com.isaiajereb.gymandgram.persistencia.room.mapper.DiaMapper;

import java.util.List;
import java.util.UUID;

public class DiaRoomDataSource implements DiaDataSource {
    private final DiaDAO diaDAO;

    public DiaRoomDataSource(final Context context){
        AppDatabase db = AppDatabase.getInstance(context);
        diaDAO = db.diaDAO();
    }

    @Override
    public void guardarDia(Dia dia, OnResult<Void> callback) {
        try{
            DiaEntity entity = DiaMapper.toEntity(dia);
            diaDAO.guardarDia(entity);
            callback.onSuccess(null);
        }
        catch (Exception e){
            callback.onError(e);
        }
    }

    @Override
    public void getDias(UUID rutinaID, OnResult<List<Dia>> callback) {
        try{
            List<DiaEntity> entities = diaDAO.getDias(rutinaID);
            List<Dia> dias = DiaMapper.fromEntities(entities);
            callback.onSuccess(dias);
        }
        catch (Exception e){
            callback.onError(e);
        }
    }

    @Override
    public void cambiarHora(Dia dia, OnResult<Void> callback) {
        try{
            DiaEntity entity = DiaMapper.toEntity(dia);
            diaDAO.editarDia(entity);
            callback.onSuccess(null);
        }
        catch (Exception e){
            callback.onError(e);
        }
    }
}
