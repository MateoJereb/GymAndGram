package com.isaiajereb.gymandgram.repo;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import com.isaiajereb.gymandgram.model.Dia;
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

    private OnResult<Void> voidCallback = new OnResult<Void>() {
        @Override
        public void onSuccess(Void result) { }

        @Override
        public void onError(Throwable exception) { Log.e("RutinasRepository","Void callback error"); }
    };

    public static Rutina getRutinaInicial(){
        Rutina r = new Rutina();
        r.setId(UUID.randomUUID());
        r.setNombre("Mi primera rutina");
        r.setActual(false);

        return r;
    }
}
