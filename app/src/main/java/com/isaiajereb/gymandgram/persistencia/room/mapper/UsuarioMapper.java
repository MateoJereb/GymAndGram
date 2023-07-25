package com.isaiajereb.gymandgram.persistencia.room.mapper;

import com.isaiajereb.gymandgram.model.Usuario;
import com.isaiajereb.gymandgram.persistencia.room.entity.UsuarioEntity;

import java.util.List;
import java.util.stream.Collectors;

public class UsuarioMapper {
    public UsuarioMapper() {
    }

    public static UsuarioEntity toEntity(final Usuario usuario){
        return new UsuarioEntity(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getMail(),
                usuario.getGenero(),
                usuario.getEdad(),
                usuario.getPassword()
        );
    }

    public static Usuario fromEntity(final UsuarioEntity entity){
        return new Usuario(
                entity.getId(),
                entity.getNombre(),
                entity.getMail(),
                entity.getGenero(),
                entity.getEdad(),
                entity.getPassword()
        );
    }

    public static List<UsuarioEntity> toEntities(final List<Usuario> lista){
        return lista.stream().map(u -> toEntity(u)).collect(Collectors.toList());
    }

    public static List<Usuario> fromEntities(final List<UsuarioEntity> lista){
        return lista.stream().map(u -> fromEntity(u)).collect(Collectors.toList());
    }
}
