package com.isaiajereb.gymandgram.persistencia.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.isaiajereb.gymandgram.persistencia.room.entity.RutinaEntity;

import java.util.List;
import java.util.UUID;

@Dao
public interface RutinaDAO {
    @Insert
    void guardarRutina(RutinaEntity rutina);

    @Query("SELECT * FROM rutina WHERE id_usuario=:userID")
    List<RutinaEntity> getRutinas(UUID userID);

    @Query("SELECT * FROM rutina WHERE id=:rutinaID")
    RutinaEntity getRutinaById(UUID rutinaID);

    @Query("UPDATE rutina SET actual = 0 WHERE actual = 1")
    void desmarcarActual();

    @Update
    void editarRutina(RutinaEntity rutina);

    @Delete
    void eliminarRutina(RutinaEntity rutina);
}
