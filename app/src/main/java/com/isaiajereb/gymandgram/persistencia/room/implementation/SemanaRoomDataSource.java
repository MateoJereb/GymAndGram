package com.isaiajereb.gymandgram.persistencia.room.implementation ;

import android.content.Context;

import com.isaiajereb.gymandgram.model.Semana;
import com.isaiajereb.gymandgram.persistencia.OnResult;
import com.isaiajereb.gymandgram.persistencia.datasource_interfaces.SemanaDataSource;
import com.isaiajereb.gymandgram.persistencia.room.AppDatabase;
import com.isaiajereb.gymandgram.persistencia.room.dao.SemanaDAO;
import com.isaiajereb.gymandgram.persistencia.room.entity.SemanaEntity;
import com.isaiajereb.gymandgram.persistencia.room.mapper.SemanaMapper;

import java.util.List;
import java.util.UUID;

public class SemanaRoomDataSource implements SemanaDataSource {
    private final SemanaDAO semanaDAO;

    public SemanaRoomDataSource(final Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        semanaDAO = db.semanaDAO();
    }

    @Override
    public void guardarSemana(Semana semana, OnResult<Void> callback) {
        try{
            SemanaEntity entity = SemanaMapper.toEntity(semana);
            semanaDAO.guardarSemana(entity);
            callback.onSuccess(null);
        }
        catch (Exception e){
            callback.onError(e);
        }
    }

    @Override
    public void getSemanas(UUID rutinaID, OnResult<List<Semana>> callback) {
        try{
            List<SemanaEntity> entities = semanaDAO.getSemanas(rutinaID);
            List<Semana> semanas = SemanaMapper.fromEntities(entities);
            callback.onSuccess(semanas);
        }
        catch (Exception e){
            callback.onError(e);
        }
    }

    @Override
    public void eliminarSemana(Semana semana, OnResult<Void> callback) {
        try{
            SemanaEntity entity = SemanaMapper.toEntity(semana);
            semanaDAO.eliminarSemana(entity);
            callback.onSuccess(null);
        }
        catch (Exception e){
            callback.onError(e);
        }
    }
}
