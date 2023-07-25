package com.isaiajereb.gymandgram.persistencia.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.isaiajereb.gymandgram.persistencia.room.dao.DiaDAO;
import com.isaiajereb.gymandgram.persistencia.room.dao.EjercicioDAO;
import com.isaiajereb.gymandgram.persistencia.room.dao.RutinaDAO;
import com.isaiajereb.gymandgram.persistencia.room.dao.UsuarioDAO;
import com.isaiajereb.gymandgram.persistencia.room.entity.DiaEntity;
import com.isaiajereb.gymandgram.persistencia.room.entity.EjercicioEntity;
import com.isaiajereb.gymandgram.persistencia.room.entity.RutinaEntity;
import com.isaiajereb.gymandgram.persistencia.room.entity.UsuarioEntity;
import com.isaiajereb.gymandgram.persistencia.room.mapper.RutinaMapper;
import com.isaiajereb.gymandgram.repo.RutinasRepository;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(  entities = {UsuarioEntity.class, RutinaEntity.class, DiaEntity.class, EjercicioEntity.class},
            version = 1,
            exportSchema = false)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UsuarioDAO usuarioDAO();
    public abstract RutinaDAO rutinaDAO();
    public abstract DiaDAO diaDAO();
    public abstract EjercicioDAO ejercicioDAO();

    public static AppDatabase instance;
    public static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static synchronized AppDatabase getInstance(final Context context){
        if(instance == null){
            instance = buildDatabase(context);
        }
        return instance;
    }

    private static AppDatabase buildDatabase(final Context context){
        return Room.databaseBuilder(context,AppDatabase.class,"gymandgram_localdb").addCallback(new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        //Crear el usuario inicial solo con una id
                        UUID userId = UUID.randomUUID();
                        UsuarioEntity usuario = new UsuarioEntity(userId,null,null,null,null,null);
                        getInstance(context).usuarioDAO().guardarUsuario(usuario);
                    }
                });
            }
        }).build();
    }
}
