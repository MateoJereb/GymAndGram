package com.isaiajereb.gymandgram.repo;

import android.content.Context;

import com.isaiajereb.gymandgram.model.Usuario;
import com.isaiajereb.gymandgram.persistencia.OnResult;
import com.isaiajereb.gymandgram.persistencia.datasource_interfaces.UsuarioDataSource;
import com.isaiajereb.gymandgram.persistencia.room.implementation.UsuarioRoomDataSource;

public class UsuariosRepository {

    final UsuarioDataSource usuarioDataSource;

    public UsuariosRepository(Context context){
        usuarioDataSource = new UsuarioRoomDataSource(context);
    }

    public void guardarUsuario(Usuario usuario, OnResult<Void> callback) {
        usuarioDataSource.guardarUsuario(usuario,callback);
    }

    public void getUsuario(OnResult<Usuario> callback) {
        usuarioDataSource.getUsuario(callback);
    }

    public void editarUsuario(Usuario usuario, OnResult<Void> callback) {
        usuarioDataSource.editarUsuario(usuario,callback);
    }

}
