package com.isaiajereb.gymandgram.persistencia.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.isaiajereb.gymandgram.persistencia.room.entity.EjercicioEntity;

import java.util.List;
import java.util.UUID;

@Dao
public interface EjercicioDAO {
    @Insert
    void guardarEjercicio(EjercicioEntity ejercicio);

    @Insert
    void guardarEjercicios(List<EjercicioEntity> ejercicios);

    @Query("SELECT * FROM ejercicio WHERE id_dia IN (:diasIDs)")
    List<EjercicioEntity> getEjercicios(List<UUID> diasIDs);

    @Update
    void editarEjercicio(EjercicioEntity ejercicio);

    @Delete
    void eliminarEjercicio(EjercicioEntity ejercicio);

    @Delete
    void eliminarEjercicios(List<EjercicioEntity> ejercicios);
}
