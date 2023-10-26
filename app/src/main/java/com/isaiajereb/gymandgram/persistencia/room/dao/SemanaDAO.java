package com.isaiajereb.gymandgram.persistencia.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.isaiajereb.gymandgram.persistencia.room.entity.SemanaEntity;

import java.util.List;
import java.util.UUID;

@Dao
public interface SemanaDAO {
    @Insert
    void guardarSemana(SemanaEntity semana);

    @Insert
    void guardarSemanas(List<SemanaEntity> semanas);

    @Query("SELECT * FROM semana WHERE id_rutina=:rutinaID")
    List<SemanaEntity> getSemanas(UUID rutinaID);

    @Update
    void editarSemana(SemanaEntity semana);

    @Delete
    void eliminarSemana(SemanaEntity semana);

    @Delete
    void eliminarSemanas(List<SemanaEntity> semanas);
}
