package com.isaiajereb.gymandgram.persistencia.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.isaiajereb.gymandgram.persistencia.room.entity.RutinaEntity;

import java.util.List;
import java.util.UUID;

@Dao
public abstract class RutinaDAO {
    @Insert
    public abstract void guardarRutina(RutinaEntity rutina);

    @Query("SELECT * FROM rutina WHERE id_usuario=:userID")
    public abstract List<RutinaEntity> getRutinas(UUID userID);

    @Query("SELECT * FROM rutina WHERE id=:rutinaID")
    public abstract RutinaEntity getRutinaById(UUID rutinaID);

    @Query("UPDATE rutina SET actual = 0 WHERE actual = 1")
    public abstract void desmarcarActual();

    @Update
    public abstract void editarRutina(RutinaEntity rutina);

    @Transaction
    public void marcarActual(RutinaEntity rutina){
        desmarcarActual();
        editarRutina(rutina);
    }

    @Delete
    public abstract void eliminarRutina(RutinaEntity rutina);
}
