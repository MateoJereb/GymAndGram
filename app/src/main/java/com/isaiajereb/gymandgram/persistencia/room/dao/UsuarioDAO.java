package com.isaiajereb.gymandgram.persistencia.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.isaiajereb.gymandgram.persistencia.room.entity.UsuarioEntity;

import java.util.List;
import java.util.UUID;

@Dao
public interface UsuarioDAO {
    @Insert
    void guardarUsuario(UsuarioEntity usuario);

    @Query("SELECT * FROM usuario")
    UsuarioEntity getUser();

    @Update
    void editarUsuario(UsuarioEntity usuario);
}
