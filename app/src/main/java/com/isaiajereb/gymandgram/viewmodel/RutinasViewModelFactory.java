package com.isaiajereb.gymandgram.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.isaiajereb.gymandgram.model.Usuario;
import com.isaiajereb.gymandgram.repo.RutinasRepository;

public class RutinasViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;
    private Usuario usuario;

    public RutinasViewModelFactory(final Context context, Usuario usuario){
        this.context = context;
        this.usuario = usuario;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(RutinasViewModel.class)){
            final RutinasRepository rutinasRepository = new RutinasRepository(context,usuario);

            return (T) new RutinasViewModel(rutinasRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
