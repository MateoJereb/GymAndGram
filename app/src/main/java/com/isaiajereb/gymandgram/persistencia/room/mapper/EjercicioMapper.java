package com.isaiajereb.gymandgram.persistencia.room.mapper;

import com.isaiajereb.gymandgram.model.Ejercicio;
import com.isaiajereb.gymandgram.persistencia.room.entity.EjercicioEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EjercicioMapper {
    public static EjercicioEntity toEntity(Ejercicio ejercicio){
        return new EjercicioEntity(
                ejercicio.getId(),
                ejercicio.getNombre(),
                ejercicio.getPosicion(),
                (ejercicio.getSeries().isPresent()) ? ejercicio.getSeries().get() : null,
                (ejercicio.getRepeticiones().isPresent()) ? ejercicio.getRepeticiones().get() : null,
                (ejercicio.getPeso().isPresent()) ? ejercicio.getPeso().get() : null,
                (ejercicio.getTiempo_cantidad().isPresent()) ? ejercicio.getTiempo_cantidad().get() : null,
                (ejercicio.getTiempo_unidad().isPresent()) ? ejercicio.getTiempo_unidad().get() : null,
                ejercicio.getObservaciones(),
                ejercicio.getId_dia()
        );
    }

    public static Ejercicio fromEntity(EjercicioEntity entity){
        return new Ejercicio(
                entity.getId(),
                entity.getNombre(),
                entity.getPosicion(),
                Optional.ofNullable(entity.getSeries()),
                Optional.ofNullable(entity.getRepeticiones()),
                Optional.ofNullable(entity.getPeso()),
                Optional.ofNullable(entity.getTiempo_cantidad()),
                Optional.ofNullable(entity.getTiempo_unidad()),
                entity.getObservaciones(),
                entity.getId_dia()
        );
    }

    public static List<EjercicioEntity> toEntities(List<Ejercicio> lista){
        return lista.stream().map(e -> toEntity(e)).collect(Collectors.toList());
    }

    public static  List<Ejercicio> fromEntities(List<EjercicioEntity> lista){
        return lista.stream().map(e -> fromEntity(e)).collect(Collectors.toList());
    }
}
