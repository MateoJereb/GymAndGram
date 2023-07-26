package com.isaiajereb.gymandgram.persistencia.room.mapper;

import com.isaiajereb.gymandgram.model.Semana;
import com.isaiajereb.gymandgram.persistencia.room.entity.SemanaEntity;

import java.util.List;
import java.util.stream.Collectors;

public class SemanaMapper {
    public static SemanaEntity toEntity(Semana semana){
        return new SemanaEntity(
                semana.getId(),
                semana.getNumero(),
                semana.getId_rutina()
        );
    }

    public static Semana fromEntity(SemanaEntity entity){
        return new Semana(
                entity.getId(),
                entity.getNumero(),
                entity.getId_rutina()
        );
    }

    public static List<SemanaEntity> toEntities(List<Semana> lista){
        return lista.stream().map(s -> toEntity(s)).collect(Collectors.toList());
    }

    public static List<Semana> fromEntities(List<SemanaEntity> lista){
        return lista.stream().map(e -> fromEntity(e)).collect(Collectors.toList());
    }
}
