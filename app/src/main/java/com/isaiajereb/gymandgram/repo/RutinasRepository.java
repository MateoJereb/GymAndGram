package com.isaiajereb.gymandgram.repo;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import com.isaiajereb.gymandgram.model.Dia;
import com.isaiajereb.gymandgram.model.DiaSemana;
import com.isaiajereb.gymandgram.model.Ejercicio;
import com.isaiajereb.gymandgram.model.Rutina;
import com.isaiajereb.gymandgram.model.Semana;
import com.isaiajereb.gymandgram.model.UnidadTiempo;
import com.isaiajereb.gymandgram.model.Usuario;
import com.isaiajereb.gymandgram.persistencia.OnResult;
import com.isaiajereb.gymandgram.persistencia.datasource_interfaces.DiaDataSource;
import com.isaiajereb.gymandgram.persistencia.datasource_interfaces.EjercicioDataSource;
import com.isaiajereb.gymandgram.persistencia.datasource_interfaces.RutinaDataSource;
import com.isaiajereb.gymandgram.persistencia.datasource_interfaces.SemanaDataSource;
import com.isaiajereb.gymandgram.persistencia.room.implementation.DiaRoomDataSource;
import com.isaiajereb.gymandgram.persistencia.room.implementation.EjercicioRoomDataSource;
import com.isaiajereb.gymandgram.persistencia.room.implementation.RutinaRoomDataSource;
import com.isaiajereb.gymandgram.persistencia.room.implementation.SemanaRoomDataSource;
import com.isaiajereb.gymandgram.viewmodel.UsuarioViewModel;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class RutinasRepository {

    private RutinaDataSource rutinaDataSource;
    private SemanaDataSource semanaDataSource;
    private DiaDataSource diaDataSource;
    private EjercicioDataSource ejercicioDataSource;
    private Usuario usuario;

    private OnResult<LinkedHashMap<String,Object>> datosRutinaCargadosCallback;
    private LinkedHashMap<String,Object> datosRutina;
    private OnResult<List<Dia>> diasRutinaActualCallback;

    public RutinasRepository(Context context, Usuario usuario) {
        rutinaDataSource = new RutinaRoomDataSource(context);
        semanaDataSource = new SemanaRoomDataSource(context);
        diaDataSource = new DiaRoomDataSource(context);
        ejercicioDataSource = new EjercicioRoomDataSource(context);

        this.usuario = usuario;
    }

    public void recuperarRutinas(OnResult<List<Rutina>> callback){
        rutinaDataSource.getRutinas(usuario.getId(),callback);
    }

    //El callback retorna una map de objects con las listas de semanas, dias y ejercicios
    public void recuperarDatosRutina(Rutina rutina, OnResult<LinkedHashMap<String,Object>> callback){
        datosRutinaCargadosCallback = callback;
        datosRutina = new LinkedHashMap<>();

        semanaDataSource.getSemanas(rutina.getId(),semanasCargadasCallback);
    }

    public void guardarRutinaCompleta(Rutina rutina, List<Semana> semanas, List<Dia> dias, List<Ejercicio> ejercicios, OnResult<Void> callback){
       rutinaDataSource.guardarRutinaCompleta(rutina,semanas,dias,ejercicios,callback);
    }

    public void editarRutinaCompleta(Rutina rutina, List<Semana> semanas, List<Dia> dias, List<Ejercicio> ejercicios, OnResult<Void> callback){
        rutinaDataSource.editarRutinaCompleta(rutina,semanas,dias,ejercicios,callback);
    }

    public void eliminarRutina(Rutina rutina, OnResult<Void> callback){
        rutinaDataSource.eliminarRutina(rutina,callback);
    }

    public void buscarDiasRutinaActual(Rutina rutina, OnResult<List<Dia>> callback){
        diasRutinaActualCallback = callback;
        recuperarDatosRutina(rutina,datosRutinaActualCargadosCallback);
    }

    private OnResult<List<Semana>> semanasCargadasCallback = new OnResult<List<Semana>>() {
        @Override
        public void onSuccess(List<Semana> result) {
            datosRutina.put("semanas",result);
            List<UUID> idsSemanas = result.stream().map(s -> s.getId()).collect(Collectors.toList());

            diaDataSource.getDias(idsSemanas,diasCargadosCallback);
        }

        @Override
        public void onError(Throwable exception) {
            Log.e("RutinasRepository","Error al buscar las semanas");
            datosRutinaCargadosCallback.onError(exception);
        }
    };

    private OnResult<List<Dia>> diasCargadosCallback = new OnResult<List<Dia>>() {
        @Override
        public void onSuccess(List<Dia> result) {
            datosRutina.put("dias",result);
            List<UUID> idsDias = result.stream().map(d -> d.getId()).collect(Collectors.toList());

            ejercicioDataSource.getEjercicios(idsDias,ejerciciosCargadosCallback);
        }

        @Override
        public void onError(Throwable exception) {
            Log.e("RutinasRepository","Error al buscar los dias");
            datosRutinaCargadosCallback.onError(exception);
        }
    };

    private OnResult<List<Ejercicio>> ejerciciosCargadosCallback = new OnResult<List<Ejercicio>>() {
        @Override
        public void onSuccess(List<Ejercicio> result) {
            datosRutina.put("ejercicios",result);
            datosRutinaCargadosCallback.onSuccess(datosRutina);
        }

        @Override
        public void onError(Throwable exception) {
            Log.e("RutinasRepository","Error al buscar los ejercicios");
            datosRutinaCargadosCallback.onError(exception);
        }
    };

    private OnResult<LinkedHashMap<String,Object>> datosRutinaActualCargadosCallback = new OnResult<LinkedHashMap<String, Object>>() {
        @Override
        public void onSuccess(LinkedHashMap<String, Object> result) {
            List<Dia> diasConEjercicios = new ArrayList<>();

            List<Semana> semanas = (List<Semana>) result.get("semanas");
            List<Dia> dias = (List<Dia>) result.get("dias");
            List<Ejercicio> ejercicios = (List<Ejercicio>) result.get("ejercicios");

            Semana ultimaSemana = semanas.get(semanas.size()-1);
            List<Dia> diasUltimaSemana = dias.stream().filter(d -> d.getId_semana().equals(ultimaSemana.getId())).collect(Collectors.toList());

            for(Dia dia : diasUltimaSemana){
                List<Ejercicio> ejerciciosDia = ejercicios.stream().filter(e -> e.getId_dia().equals(dia.getId())).collect(Collectors.toList());

                if(ejerciciosDia.size() > 0) diasConEjercicios.add(dia);
            }

            diasRutinaActualCallback.onSuccess(diasConEjercicios);
        }

        @Override
        public void onError(Throwable exception) {
            Log.e("RutinasRepository","Error al buscar datos rutina actual");
        }
    };

    private OnResult<Void> voidCallback = new OnResult<Void>() {
        @Override
        public void onSuccess(Void result) { }

        @Override
        public void onError(Throwable exception) { Log.e("RutinasRepository","Void callback error"); }
    };

    private static UUID idRutina = UUID.randomUUID();

    private static UUID idSemana1 = UUID.randomUUID();
    private static UUID idSemana2 = UUID.randomUUID();

    private static UUID idLunes1 = UUID.randomUUID();
    private static UUID idMartes1 = UUID.randomUUID();
    private static UUID idMiercoles1 = UUID.randomUUID();
    private static UUID idJueves1 = UUID.randomUUID();
    private static UUID idViernes1 = UUID.randomUUID();
    private static UUID idSabado1 = UUID.randomUUID();
    private static UUID idDomingo1 = UUID.randomUUID();

    private static UUID idLunes2 = UUID.randomUUID();
    private static UUID idMartes2 = UUID.randomUUID();
    private static UUID idMiercoles2 = UUID.randomUUID();
    private static UUID idJueves2 = UUID.randomUUID();
    private static UUID idViernes2 = UUID.randomUUID();
    private static UUID idSabado2 = UUID.randomUUID();
    private static UUID idDomingo2 = UUID.randomUUID();

    public static Rutina getRutinaInicial(){
        Rutina r = new Rutina();
        r.setId(idRutina);
        r.setNombre("Mi primera rutina");
        r.setActual(true);
        r.setFechaCreacion(LocalDateTime.now());
        r.setFechaUltimaModificacion(LocalDateTime.now());

        return r;
    }

    public static List<Semana> getSemanasIniciales(){
        List<Semana> lista = new ArrayList<>();

        Semana sem1 = new Semana(idSemana1,1,idRutina);
        lista.add(sem1);

        Semana sem2 = new Semana(idSemana2,2,idRutina);
        lista.add(sem2);

        return lista;
    }

    public static List<Dia> getDiasIniciales(){
        List<Dia> lista =  new ArrayList<>();

        Dia lunes1 = new Dia(idLunes1, DiaSemana.Lunes, LocalTime.of(16,0),idSemana1);
        lista.add(lunes1);

        Dia martes1 = new Dia(idMartes1, DiaSemana.Martes, LocalTime.of(0,0), idSemana1);
        lista.add(martes1);

        Dia miercoles1 = new Dia(idMiercoles1, DiaSemana.Miercoles, LocalTime.of(0,0), idSemana1);
        lista.add(miercoles1);

        Dia jueves1 = new Dia(idJueves1, DiaSemana.Jueves, LocalTime.of(0,0), idSemana1);
        lista.add(jueves1);

        Dia viernes1 = new Dia(idViernes1, DiaSemana.Viernes, LocalTime.of(0,0), idSemana1);
        lista.add(viernes1);

        Dia sabado1 = new Dia(idSabado1, DiaSemana.Sabado, LocalTime.of(0,0), idSemana1);
        lista.add(sabado1);

        Dia domingo1 = new Dia(idDomingo1, DiaSemana.Domingo, LocalTime.of(0,0), idSemana1);
        lista.add(domingo1);

        Dia lunes2 = new Dia(idLunes2, DiaSemana.Lunes, LocalTime.of(16,0),idSemana2);
        lista.add(lunes2);

        Dia martes2 = new Dia(idMartes2, DiaSemana.Martes, LocalTime.of(0,0), idSemana2);
        lista.add(martes2);

        Dia miercoles2 = new Dia(idMiercoles2, DiaSemana.Miercoles, LocalTime.of(0,0), idSemana2);
        lista.add(miercoles2);

        Dia jueves2 = new Dia(idJueves2, DiaSemana.Jueves, LocalTime.of(0,0), idSemana2);
        lista.add(jueves2);

        Dia viernes2 = new Dia(idViernes2, DiaSemana.Viernes, LocalTime.of(0,0), idSemana2);
        lista.add(viernes2);

        Dia sabado2 = new Dia(idSabado2, DiaSemana.Sabado, LocalTime.of(0,0), idSemana2);
        lista.add(sabado2);

        Dia domingo2 = new Dia(idDomingo2, DiaSemana.Domingo, LocalTime.of(0,0), idSemana2);
        lista.add(domingo2);

        return lista;
    }

    public static List<Ejercicio> getEjerciciosIniciales(){
        List<Ejercicio> lista = new ArrayList<>();

        Ejercicio ej1lunes1 = new Ejercicio();
        ej1lunes1.setId(UUID.randomUUID());
        ej1lunes1.setNombre("Abdominales inferiores");
        ej1lunes1.setPosicion(0);
        ej1lunes1.setRepeticiones(Optional.of(12));
        ej1lunes1.setSeries(Optional.of(3));
        ej1lunes1.setId_dia(idLunes1);
        lista.add(ej1lunes1);

        Ejercicio ej2lunes1 = new Ejercicio();
        ej2lunes1.setId(UUID.randomUUID());
        ej2lunes1.setNombre("Planchas sobre antebrazos");
        ej2lunes1.setPosicion(1);
        ej2lunes1.setSeries(Optional.of(3));
        ej2lunes1.setTiempo_cantidad(Optional.of(30.0));
        ej2lunes1.setTiempo_unidad(Optional.of(UnidadTiempo.Segundo));
        ej2lunes1.setId_dia(idLunes1);
        lista.add(ej2lunes1);

        Ejercicio ej3lunes1 = new Ejercicio();
        ej3lunes1.setId(UUID.randomUUID());
        ej3lunes1.setNombre("Pecho banco plano");
        ej3lunes1.setPosicion(2);
        ej3lunes1.setRepeticiones(Optional.of(12));
        ej3lunes1.setSeries(Optional.of(3));
        ej3lunes1.setPeso(Optional.of(15.0));
        ej3lunes1.setId_dia(idLunes1);
        lista.add(ej3lunes1);

        Ejercicio ej4lunes1 = new Ejercicio();
        ej4lunes1.setId(UUID.randomUUID());
        ej4lunes1.setPosicion(3);
        ej4lunes1.setNombre("Vuelos laterales");
        ej4lunes1.setRepeticiones(Optional.of(12));
        ej4lunes1.setSeries(Optional.of(3));
        ej4lunes1.setPeso(Optional.of(7.5));
        ej4lunes1.setId_dia(idLunes1);
        lista.add(ej4lunes1);

        Ejercicio ej5lunes1 = new Ejercicio();
        ej5lunes1.setId(UUID.randomUUID());
        ej5lunes1.setNombre("Bici");
        ej5lunes1.setPosicion(4);
        ej5lunes1.setTiempo_cantidad(Optional.of(15.0));
        ej5lunes1.setTiempo_unidad(Optional.of(UnidadTiempo.Minuto));
        ej5lunes1.setId_dia(idLunes1);
        lista.add(ej5lunes1);

        Ejercicio ej1lunes2 = new Ejercicio();
        ej1lunes2.setId(UUID.randomUUID());
        ej1lunes2.setNombre("Abdominales inferiores");
        ej1lunes2.setPosicion(0);
        ej1lunes2.setRepeticiones(Optional.of(15));
        ej1lunes2.setSeries(Optional.of(3));
        ej1lunes2.setId_dia(idLunes2);
        lista.add(ej1lunes2);

        Ejercicio ej2lunes2 = new Ejercicio();
        ej2lunes2.setId(UUID.randomUUID());
        ej2lunes2.setNombre("Planchas sobre antebrazos");
        ej2lunes2.setPosicion(1);
        ej2lunes2.setSeries(Optional.of(3));
        ej2lunes2.setTiempo_cantidad(Optional.of(45.0));
        ej2lunes2.setTiempo_unidad(Optional.of(UnidadTiempo.Segundo));
        ej2lunes2.setId_dia(idLunes2);
        lista.add(ej2lunes2);

        Ejercicio ej3lunes2 = new Ejercicio();
        ej3lunes2.setId(UUID.randomUUID());
        ej3lunes2.setNombre("Pecho banco plano");
        ej3lunes2.setPosicion(2);
        ej3lunes2.setRepeticiones(Optional.of(10));
        ej3lunes2.setSeries(Optional.of(3));
        ej3lunes2.setPeso(Optional.of(17.5));
        ej3lunes2.setId_dia(idLunes2);
        lista.add(ej3lunes2);

        Ejercicio ej4lunes2 = new Ejercicio();
        ej4lunes2.setId(UUID.randomUUID());
        ej4lunes2.setNombre("Vuelos laterales");
        ej4lunes2.setPosicion(3);
        ej4lunes2.setRepeticiones(Optional.of(10));
        ej4lunes2.setSeries(Optional.of(3));
        ej4lunes2.setPeso(Optional.of(10.0));
        ej4lunes2.setId_dia(idLunes2);
        lista.add(ej4lunes2);


        return lista;
    }
}
