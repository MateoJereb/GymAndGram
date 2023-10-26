package com.isaiajereb.gymandgram.persistencia.room.implementation;

import android.content.Context;

import com.isaiajereb.gymandgram.model.Dia;
import com.isaiajereb.gymandgram.model.Ejercicio;
import com.isaiajereb.gymandgram.model.Rutina;
import com.isaiajereb.gymandgram.model.Semana;
import com.isaiajereb.gymandgram.persistencia.OnResult;
import com.isaiajereb.gymandgram.persistencia.datasource_interfaces.RutinaDataSource;
import com.isaiajereb.gymandgram.persistencia.room.AppDatabase;
import com.isaiajereb.gymandgram.persistencia.room.dao.RutinaDAO;
import com.isaiajereb.gymandgram.persistencia.room.entity.DiaEntity;
import com.isaiajereb.gymandgram.persistencia.room.entity.EjercicioEntity;
import com.isaiajereb.gymandgram.persistencia.room.entity.RutinaEntity;
import com.isaiajereb.gymandgram.persistencia.room.entity.SemanaEntity;
import com.isaiajereb.gymandgram.persistencia.room.mapper.DiaMapper;
import com.isaiajereb.gymandgram.persistencia.room.mapper.EjercicioMapper;
import com.isaiajereb.gymandgram.persistencia.room.mapper.RutinaMapper;
import com.isaiajereb.gymandgram.persistencia.room.mapper.SemanaMapper;

import java.util.List;
import java.util.UUID;

public class RutinaRoomDataSource implements RutinaDataSource {
    private final RutinaDAO rutinaDAO;

    public RutinaRoomDataSource(final Context context){
        AppDatabase db = AppDatabase.getInstance(context);
        rutinaDAO = db.rutinaDAO();
    }

    @Override
    public void guardarRutina(Rutina rutina, OnResult<Void> callback) {
        try{
            RutinaEntity entity = RutinaMapper.toEntity(rutina);
            rutinaDAO.guardarRutina(entity);
            callback.onSuccess(null);
        }
        catch (Exception e){
            callback.onError(e);
        }
    }

    @Override
    public void guardarRutinaCompleta(Rutina rutina, List<Semana> semanas, List<Dia> dias, List<Ejercicio> ejercicios, OnResult<Void> callback) {
        try{
            RutinaEntity rutinaEntity = RutinaMapper.toEntity(rutina);
            List<SemanaEntity> semanaEntities = SemanaMapper.toEntities(semanas);
            List<DiaEntity> diaEntities = DiaMapper.toEntities(dias);
            List<EjercicioEntity> ejercicioEntities = EjercicioMapper.toEntities(ejercicios);

            rutinaDAO.guardarRutinaCompleta(rutinaEntity,semanaEntities,diaEntities,ejercicioEntities);
            callback.onSuccess(null);
        }
        catch (Exception e){
            callback.onError(e);
        }
    }

    @Override
    public void editarRutinaCompleta(Rutina rutina, List<Semana> semanas, List<Dia> dias, List<Ejercicio> ejercicios, OnResult<Void> callback) {
        try{
            RutinaEntity rutinaEntity = RutinaMapper.toEntity(rutina);
            List<SemanaEntity> semanaEntities = SemanaMapper.toEntities(semanas);
            List<DiaEntity> diaEntities = DiaMapper.toEntities(dias);
            List<EjercicioEntity> ejercicioEntities = EjercicioMapper.toEntities(ejercicios);

            rutinaDAO.editarRutinaCompleta(rutinaEntity,semanaEntities,diaEntities,ejercicioEntities);
            callback.onSuccess(null);
        }
        catch(Exception e){
            callback.onError(e);
        }
    }

    @Override
    public void getRutinas(UUID userID, OnResult<List<Rutina>> callback) {
        try{
            List<RutinaEntity> entities = rutinaDAO.getRutinas(userID);
            List<Rutina> rutinas = RutinaMapper.fromEntities(entities);
            callback.onSuccess(rutinas);
        }
        catch (Exception e){
            callback.onError(e);
        }
    }

    @Override
    public void getRutinaById(UUID rutinaID, OnResult<Rutina> callback) {
        try{
            RutinaEntity entity = rutinaDAO.getRutinaById(rutinaID);
            Rutina rutina = RutinaMapper.fromEntity(entity);
            callback.onSuccess(rutina);
        }
        catch (Exception e){
            callback.onError(e);
        }
    }

    @Override
    public void eliminarRutina(Rutina rutina, OnResult<Void> callback) {
        try{
            RutinaEntity entity = RutinaMapper.toEntity(rutina);
            rutinaDAO.eliminarRutina(entity);
            callback.onSuccess(null);
        }
        catch (Exception e){
            callback.onError(e);
        }
    }
}
