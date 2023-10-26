package com.isaiajereb.gymandgram.persistencia.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.isaiajereb.gymandgram.model.Ejercicio;
import com.isaiajereb.gymandgram.persistencia.room.entity.DiaEntity;
import com.isaiajereb.gymandgram.persistencia.room.entity.EjercicioEntity;
import com.isaiajereb.gymandgram.persistencia.room.entity.RutinaEntity;
import com.isaiajereb.gymandgram.persistencia.room.entity.SemanaEntity;

import java.util.List;
import java.util.UUID;

@Dao
public abstract class RutinaDAO {
    @Insert
    public abstract void guardarRutina(RutinaEntity rutina);

    @Insert
    public abstract void guardarSemanas(List<SemanaEntity> semanas);

    @Insert
    public abstract void guardarDias(List<DiaEntity> dias);

    @Insert
    public abstract void guardarEjercicios(List<EjercicioEntity> ejercicios);

    @Transaction
    public void guardarRutinaCompleta(RutinaEntity rutina, List<SemanaEntity> semanas, List<DiaEntity> dias, List<EjercicioEntity> ejercicios){
        //Si se va a marcar como actual, desmarcar la otra
        if(rutina.getActual()) desmarcarActual();

        guardarRutina(rutina);
        guardarSemanas(semanas);
        guardarDias(dias);
        guardarEjercicios(ejercicios);
    }

    @Query("SELECT * FROM rutina WHERE id_usuario=:userID")
    public abstract List<RutinaEntity> getRutinas(UUID userID);

    @Query("SELECT * FROM rutina WHERE id=:rutinaID")
    public abstract RutinaEntity getRutinaById(UUID rutinaID);

    @Update
    public abstract void editarRutina(RutinaEntity rutina);

    @Transaction
    public void editarRutinaCompleta(RutinaEntity rutina, List<SemanaEntity> semanas, List<DiaEntity> dias, List<EjercicioEntity> ejercicios){
        //Si se va a marcar como actual, desmarcar la otra
        if(rutina.getActual()) desmarcarActual();

        //Se elimina la rutina => por el OnDelete.CASCADE se eliminan semanas, dias y ejercicios
        eliminarRutina(rutina);

        //Se vuelven a guardar
        guardarRutina(rutina);
        guardarSemanas(semanas);
        guardarDias(dias);
        guardarEjercicios(ejercicios);
    }

    @Transaction
    public void marcarActualYDesmarcarOtra(RutinaEntity rutina){
        desmarcarActual();
        editarRutina(rutina);
    }

    @Query("UPDATE rutina SET actual = 0 WHERE actual = 1")
    public abstract void desmarcarActual();

    @Query("UPDATE rutina SET actual = 1 WHERE id=:idRutina")
    public abstract void marcarActual(UUID idRutina);

    @Delete
    public abstract void eliminarRutina(RutinaEntity rutina);
}
