package com.isaiajereb.gymandgram;

import com.isaiajereb.gymandgram.model.Dia;
import com.isaiajereb.gymandgram.model.DiaSemana;
import com.isaiajereb.gymandgram.ui.InicioFragment;

import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CalcularProximoEntrenamientoTest {
    InicioFragment inicioFragment = new InicioFragment();

    UUID idSemana = UUID.randomUUID();
    Dia dia1 = new Dia(UUID.randomUUID(), DiaSemana.Lunes, LocalTime.of(16,0), idSemana);
    Dia dia2 = new Dia(UUID.randomUUID(), DiaSemana.Miercoles, LocalTime.of(16,0), idSemana);
    Dia dia3 = new Dia(UUID.randomUUID(), DiaSemana.Viernes, LocalTime.of(16,0), idSemana);
    List<Dia> diasEntrenamiento = List.of(dia1,dia2,dia3);
    @Test
    public void horaMasTemprano(){
        /*
            Situacion:
                Se tienen varios dias, la fecha actual coincide con uno intermedio y es mas temprano que la hora configurada

            Resultado:
                Debe dar ese día a la hora configurada.
         */


        LocalDateTime fechaActual = LocalDateTime.parse("2023-10-18T14:00", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

        LocalDateTime fechaCalculada = inicioFragment.calcularProximoEntrenamiento(diasEntrenamiento,fechaActual);

        String fechaCalculadaString = fechaCalculada.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

        assertEquals(fechaCalculadaString,"2023-10-18T16:00");
    }

    @Test
    public void horaMasTarde(){
        /*
            Situacion:
                Se tienen varios dias y la fecha actual coincide con uno intermedio, pero ya paso la hora configurada
            Resultado:
                Debe dar el dia posterior mas proximo
         */

        LocalDateTime fechaActual = LocalDateTime.parse("2023-10-18T17:00", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

        LocalDateTime fechaCalculada = inicioFragment.calcularProximoEntrenamiento(diasEntrenamiento,fechaActual);

        String fechaCalculadaString = fechaCalculada.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

        assertEquals(fechaCalculadaString,"2023-10-20T16:00");
    }

    @Test
    public void diaIntermedio(){
        /*
            Situacion:
                Se tienen varios dias y la fecha actual es de un dia entre medio de ellos
            Resultado:
                Debe dar el dia posterior mas proximo
         */
        LocalDateTime fechaActual = LocalDateTime.parse("2023-10-19T17:00", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

        LocalDateTime fechaCalculada = inicioFragment.calcularProximoEntrenamiento(diasEntrenamiento,fechaActual);

        String fechaCalculadaString = fechaCalculada.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

        assertEquals(fechaCalculadaString,"2023-10-20T16:00");

    }

    @Test
    public void posteriorAlUltimoEntrenamiento(){
        /*
            Situacion:
                Se tienen varios dias y la fecha actual es posterior a todos los entrenamientos
            Resultado:
                Debe dar el primer dia de la proxima semana
         */
        LocalDateTime fechaActual = LocalDateTime.parse("2023-10-21T17:00", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

        LocalDateTime fechaCalculada = inicioFragment.calcularProximoEntrenamiento(diasEntrenamiento,fechaActual);

        String fechaCalculadaString = fechaCalculada.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

        assertEquals(fechaCalculadaString,"2023-10-23T16:00");
    }

}
