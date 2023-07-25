package com.isaiajereb.gymandgram.persistencia.datasource_interfaces;

import com.isaiajereb.gymandgram.model.Usuario;
import com.isaiajereb.gymandgram.persistencia.OnResult;

import java.util.List;

public interface UsuarioDataSource {
    void guardarUsuario(Usuario usuario, OnResult<Void> callback);

    void getUsuario(OnResult<Usuario> callback);

    void editarUsuario(Usuario usuario, OnResult<Void> callback);
}
