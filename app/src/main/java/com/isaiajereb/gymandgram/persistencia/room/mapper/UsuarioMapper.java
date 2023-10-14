package com.isaiajereb.gymandgram.persistencia.room.mapper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.isaiajereb.gymandgram.model.Usuario;
import com.isaiajereb.gymandgram.persistencia.room.entity.UsuarioEntity;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioMapper {
    public UsuarioMapper() {
    }

    public static UsuarioEntity toEntity(final Usuario usuario){
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        usuario.getFotoPerfil().compress(Bitmap.CompressFormat.PNG, 100, baos);
        return new UsuarioEntity(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getMail(),
                usuario.getGenero(),
                usuario.getEdad(),
                usuario.getPassword()/*,
                baos.toByteArray()*/
        );
    }

    public static Usuario fromEntity(final UsuarioEntity entity){
//        byte[] b = entity.getFotoPerfil();

        return new Usuario(
                entity.getId(),
                entity.getNombre(),
                entity.getMail(),
                entity.getGenero(),
                entity.getEdad(),
                entity.getPassword()/*,
                BitmapFactory.decodeByteArray(b, 0, b.length)*/
        );
    }

    public static List<UsuarioEntity> toEntities(final List<Usuario> lista){
        return lista.stream().map(u -> toEntity(u)).collect(Collectors.toList());
    }

    public static List<Usuario> fromEntities(final List<UsuarioEntity> lista){
        return lista.stream().map(u -> fromEntity(u)).collect(Collectors.toList());
    }
}
