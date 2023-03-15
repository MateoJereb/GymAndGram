package com.isaiajereb.gymandgram.persistencia.room.implementation;

import android.content.Context;

import com.isaiajereb.gymandgram.model.Usuario;
import com.isaiajereb.gymandgram.persistencia.OnResult;
import com.isaiajereb.gymandgram.persistencia.datasource_interfaces.UsuarioDataSource;
import com.isaiajereb.gymandgram.persistencia.room.AppDatabase;
import com.isaiajereb.gymandgram.persistencia.room.dao.UsuarioDAO;
import com.isaiajereb.gymandgram.persistencia.room.entity.UsuarioEntity;
import com.isaiajereb.gymandgram.persistencia.room.mapper.UsuarioMapper;

public class UsuarioRoomDataSource implements UsuarioDataSource {
    private final UsuarioDAO usuarioDAO;

    public UsuarioRoomDataSource(final Context context){
        AppDatabase db = AppDatabase.getInstance(context);
        usuarioDAO = db.usuarioDAO();
    }

    @Override
    public void guardarUsuario(Usuario usuario, OnResult<Void> callback) {
        try{
            UsuarioEntity entity = UsuarioMapper.toEntity(usuario);
            usuarioDAO.guardarUsuario(entity);
            callback.onSuccess(null);
        }
        catch (Exception e){
            callback.onError(e);
        }
    }

    @Override
    public void getUsuario(OnResult<Usuario> callback) {
        try{
            UsuarioEntity entity = usuarioDAO.getUser();
            Usuario usuario = UsuarioMapper.fromEntity(entity);
            callback.onSuccess(usuario);
        }
        catch (Exception e){
            callback.onError(e);
        }
    }

    @Override
    public void editarUsuario(Usuario usuario, OnResult<Void> callback) {
        try{
            UsuarioEntity entity = UsuarioMapper.toEntity(usuario);
            usuarioDAO.editarUsuario(entity);
            callback.onSuccess(null);
        }
        catch (Exception e){
            callback.onError(e);
        }
    }
}
