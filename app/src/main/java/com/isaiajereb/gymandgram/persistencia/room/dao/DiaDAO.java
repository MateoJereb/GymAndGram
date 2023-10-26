package com.isaiajereb.gymandgram.persistencia.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.isaiajereb.gymandgram.persistencia.room.entity.DiaEntity;

import java.util.List;
import java.util.UUID;

@Dao
public interface DiaDAO {
    @Insert
    void guardarDia(DiaEntity dia);

    @Insert
    void guardarDias(List<DiaEntity> dias);

    @Query("SELECT * FROM dia WHERE id_semana IN (:semanasIDs)")
    List<DiaEntity> getDias(List<UUID> semanasIDs);

    @Update
    void editarDia(DiaEntity dia);

    @Delete
    void eliminarDia(DiaEntity dia);

    @Delete
    void eliminarDias(List<DiaEntity> dias);
}
