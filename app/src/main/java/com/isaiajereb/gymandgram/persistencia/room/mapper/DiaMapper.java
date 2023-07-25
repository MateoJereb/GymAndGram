package com.isaiajereb.gymandgram.persistencia.room.mapper;

import com.isaiajereb.gymandgram.model.Dia;
import com.isaiajereb.gymandgram.persistencia.room.entity.DiaEntity;

import java.util.List;
import java.util.stream.Collectors;

public class DiaMapper {
    public static DiaEntity toEntity(Dia dia){
        return new DiaEntity(
                dia.getId(),
                dia.getNombre(),
                dia.getHora(),
                dia.getId_rutina()
        );
    }

    public static Dia fromEntity(DiaEntity entity){
        return new Dia(
          entity.getId(),
          entity.getNombre(),
          entity.getHora(),
          entity.getId_rutina()
        );
    }

    public static List<DiaEntity> toEntities(List<Dia> lista){
        return lista.stream().map(d -> toEntity(d)).collect(Collectors.toList());
    }

    public static List<Dia> fromEntities(List<DiaEntity> lista){
        return lista.stream().map(d -> fromEntity(d)).collect(Collectors.toList());
    }
}
