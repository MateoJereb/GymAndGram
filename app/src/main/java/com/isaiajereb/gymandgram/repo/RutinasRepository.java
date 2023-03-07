package com.isaiajereb.gymandgram.repo;

import android.content.Context;

import com.isaiajereb.gymandgram.model.Ejercicio;
import com.isaiajereb.gymandgram.model.Rutina;
import com.isaiajereb.gymandgram.model.UnidadTiempo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RutinasRepository {

    private static Ejercicio ejercicio1 = new Ejercicio(
            UUID.randomUUID(),
            "Ejercicio 1",
            Optional.of(3),
            Optional.of(12),
            Optional.ofNullable(null),
            Optional.ofNullable(null),
            Optional.ofNullable(null),
            "",
            null);

    private static Ejercicio ejercicio2 = new Ejercicio(
            UUID.randomUUID(),
            "Ejercicio 2 (solo serie)",
            Optional.of(3),
            Optional.ofNullable(null),
            Optional.ofNullable(null),
            Optional.ofNullable(null),
            Optional.ofNullable(null),
            "",
            null);

    private static Ejercicio ejercicio3 = new Ejercicio(
            UUID.randomUUID(),
            "Ejercicio 3 (solo ejercicio)",
            Optional.ofNullable(null),
            Optional.of(12),
            Optional.ofNullable(null),
            Optional.ofNullable(null),
            Optional.ofNullable(null),
            "",
            null);

    private static Ejercicio ejercicio4 = new Ejercicio(
            UUID.randomUUID(),
            "Ejercicio 4",
            Optional.of(3),
            Optional.of(12),
            Optional.of(20.0),
            Optional.ofNullable(null),
            Optional.ofNullable(null),
            "",
            null);

    private static Ejercicio ejercicio5 = new Ejercicio(
            UUID.randomUUID(),
            "Ejercicio 5",
            Optional.of(3),
            Optional.of(12),
            Optional.of(20.0),
            Optional.of(5.0),
            Optional.of(UnidadTiempo.Segundo),
            "",
            null);

    private static Ejercicio ejercicio6 = new Ejercicio(
            UUID.randomUUID(),
            "Ejercicio 6",
            Optional.ofNullable(null),
            Optional.ofNullable(null),
            Optional.of(15.0),
            Optional.of(5.0),
            Optional.of(UnidadTiempo.Minuto),
            "",
            null);

    private static Ejercicio ejercicio7 = new Ejercicio(
            UUID.randomUUID(),
            "Ejercicio 7",
            Optional.ofNullable(null),
            Optional.ofNullable(null),
            Optional.ofNullable(null),
            Optional.of(5.0),
            Optional.of(UnidadTiempo.Minuto),
            "",
            null);


    private static Rutina rutina1 = new Rutina(UUID.randomUUID(),"Rutina 1",true,null);
    private static Rutina rutina2 = new Rutina(UUID.randomUUID(),"Rutina 2",false,null);
    private static Rutina rutina3 = new Rutina(UUID.randomUUID(),"Rutina 3",false,null);
    public static final List<Ejercicio> _EJERCICIOS = List.of(ejercicio1,ejercicio2,ejercicio3,ejercicio4,ejercicio5,ejercicio6,ejercicio7);
    public static final List<Rutina> _RUTINAS = List.of(rutina1,rutina2,rutina3);

    public RutinasRepository() {

    }

}
