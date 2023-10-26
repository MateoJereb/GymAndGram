package com.isaiajereb.gymandgram.persistencia.room.implementation;

import android.content.Context;

import com.isaiajereb.gymandgram.model.Ejercicio;
import com.isaiajereb.gymandgram.persistencia.OnResult;
import com.isaiajereb.gymandgram.persistencia.datasource_interfaces.EjercicioDataSource;
import com.isaiajereb.gymandgram.persistencia.room.AppDatabase;
import com.isaiajereb.gymandgram.persistencia.room.dao.EjercicioDAO;
import com.isaiajereb.gymandgram.persistencia.room.entity.EjercicioEntity;
import com.isaiajereb.gymandgram.persistencia.room.mapper.EjercicioMapper;

import java.util.List;
import java.util.UUID;

public class EjercicioRoomDataSource implements EjercicioDataSource {
    private final EjercicioDAO ejercicioDAO;

    public EjercicioRoomDataSource(final Context context){
        AppDatabase db = AppDatabase.getInstance(context);
        ejercicioDAO = db.ejercicioDAO();
    }

    @Override
    public void guardarEjercicio(Ejercicio ejercicio, OnResult<Void> callback) {
        try{
            EjercicioEntity entity = EjercicioMapper.toEntity(ejercicio);
            ejercicioDAO.guardarEjercicio(entity);
            callback.onSuccess(null);
        }
        catch (Exception e){
            callback.onError(e);
        }
    }

    @Override
    public void guardarEjercicios(List<Ejercicio> ejercicios, OnResult<Void> callback) {
        try{
            List<EjercicioEntity> entities = EjercicioMapper.toEntities(ejercicios);
            ejercicioDAO.guardarEjercicios(entities);
            callback.onSuccess(null);
        }
        catch (Exception e){
            callback.onError(e);
        }
    }

    @Override
    public void getEjercicios(List<UUID> diasIDs, OnResult<List<Ejercicio>> callback) {
        try{
            List<EjercicioEntity> entities = ejercicioDAO.getEjercicios(diasIDs);
            List<Ejercicio> ejercicios = EjercicioMapper.fromEntities(entities);
            callback.onSuccess(ejercicios);
        }
        catch (Exception e){
            callback.onError(e);
        }
    }

    @Override
    public void editarEjercicio(Ejercicio ejercicio, OnResult<Void> callback) {
        try{
            EjercicioEntity entity = EjercicioMapper.toEntity(ejercicio);
            ejercicioDAO.editarEjercicio(entity);
            callback.onSuccess(null);
        }
        catch (Exception e){
            callback.onError(e);
        }
    }

    @Override
    public void eliminarEjercicio(Ejercicio ejercicio, OnResult<Void> callback) {
        try{
            EjercicioEntity entity = EjercicioMapper.toEntity(ejercicio);
            ejercicioDAO.eliminarEjercicio(entity);
            callback.onSuccess(null);
        }
        catch (Exception e){
            callback.onError(e);
        }
    }

    @Override
    public void eliminarEjercicios(List<Ejercicio> ejercicios, OnResult<Void> callback) {
        try{
            List<EjercicioEntity> entities = EjercicioMapper.toEntities(ejercicios);
            ejercicioDAO.eliminarEjercicios(entities);
            callback.onSuccess(null);
        }
        catch (Exception e){
            callback.onError(e);
        }
    }
}
