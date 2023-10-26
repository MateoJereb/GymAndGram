package com.isaiajereb.gymandgram.persistencia.room.mapper;

import com.isaiajereb.gymandgram.model.Rutina;
import com.isaiajereb.gymandgram.persistencia.room.entity.RutinaEntity;

import java.util.List;
import java.util.stream.Collectors;

public class RutinaMapper {
    public static RutinaEntity toEntity(final Rutina rutina){
        return new RutinaEntity(
                rutina.getId(),
                rutina.getNombre(),
                rutina.getActual(),
                rutina.getFechaCreacion(),
                rutina.getFechaUltimaModificacion(),
                rutina.getId_usuario()
        );
    }

    public static Rutina fromEntity(final RutinaEntity entity){
        return new Rutina(
                entity.getId(),
                entity.getNombre(),
                entity.getActual(),
                entity.getFechaCreacion(),
                entity.getFechaUltimaModificacion(),
                entity.getId_usuario()
        );
    }

    public static List<RutinaEntity> toEntities(final List<Rutina> lista){
        return lista.stream().map(r -> toEntity(r)).collect(Collectors.toList());
    }

    public static List<Rutina> fromEntities(final List<RutinaEntity> lista){
        return lista.stream().map(r -> fromEntity(r)).collect(Collectors.toList());
    }
}
