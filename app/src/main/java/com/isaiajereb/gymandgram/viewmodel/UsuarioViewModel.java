package com.isaiajereb.gymandgram.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.isaiajereb.gymandgram.model.Usuario;
import com.isaiajereb.gymandgram.persistencia.OnResult;
import com.isaiajereb.gymandgram.repo.UsuariosRepository;

public class UsuarioViewModel extends AndroidViewModel {
    private Usuario usuario;
    private UsuariosRepository usuariosRepository;

    public UsuarioViewModel(@NonNull Application application) {
        super(application);

        usuario = new Usuario();
        usuariosRepository = new UsuariosRepository(getApplication().getApplicationContext());

        //Guardar el usuario en memoria
        new Thread(() -> {
            usuariosRepository.getUsuario(callbackGetUsuario);
        }).start();
    }

    private OnResult<Usuario> callbackGetUsuario = new OnResult<Usuario>() {
        @Override
        public void onSuccess(Usuario result) {
            usuario = result;
        }

        @Override
        public void onError(Throwable exception) {
            Log.e("Error getUsuario()",exception.toString());
        }
    };
}
