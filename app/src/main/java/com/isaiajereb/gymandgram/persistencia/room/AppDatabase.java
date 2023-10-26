package com.isaiajereb.gymandgram.persistencia.room;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.isaiajereb.gymandgram.R;
import com.isaiajereb.gymandgram.model.Genero;
import com.isaiajereb.gymandgram.model.Semana;
import com.isaiajereb.gymandgram.persistencia.room.dao.DiaDAO;
import com.isaiajereb.gymandgram.persistencia.room.dao.EjercicioDAO;
import com.isaiajereb.gymandgram.persistencia.room.dao.RutinaDAO;
import com.isaiajereb.gymandgram.persistencia.room.dao.SemanaDAO;
import com.isaiajereb.gymandgram.persistencia.room.dao.UsuarioDAO;
import com.isaiajereb.gymandgram.persistencia.room.entity.DiaEntity;
import com.isaiajereb.gymandgram.persistencia.room.entity.EjercicioEntity;
import com.isaiajereb.gymandgram.persistencia.room.entity.RutinaEntity;
import com.isaiajereb.gymandgram.persistencia.room.entity.SemanaEntity;
import com.isaiajereb.gymandgram.persistencia.room.entity.UsuarioEntity;
import com.isaiajereb.gymandgram.persistencia.room.mapper.DiaMapper;
import com.isaiajereb.gymandgram.persistencia.room.mapper.EjercicioMapper;
import com.isaiajereb.gymandgram.persistencia.room.mapper.RutinaMapper;
import com.isaiajereb.gymandgram.persistencia.room.mapper.SemanaMapper;
import com.isaiajereb.gymandgram.repo.RutinasRepository;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(  entities = {UsuarioEntity.class, RutinaEntity.class, SemanaEntity.class, DiaEntity.class, EjercicioEntity.class},
            version = 1,
            exportSchema = false)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UsuarioDAO usuarioDAO();
    public abstract RutinaDAO rutinaDAO();
    public abstract SemanaDAO semanaDAO();
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
        return Room.databaseBuilder(context,AppDatabase.class,"gymandgram_local_db").addCallback(new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        //Crear el usuario inicial
                        UUID userId = UUID.randomUUID();
                        Drawable fotoDefault = ContextCompat.getDrawable(context, R.drawable.profile_pic);
                        Bitmap bitmap = ((BitmapDrawable)fotoDefault).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] bitmapFotoDefault = stream.toByteArray();
                        UsuarioEntity usuario = new UsuarioEntity(userId,"ADMIN","admin@gmail.com",
                                Genero.Masculino,23, "admin", bitmapFotoDefault);
                        getInstance(context).usuarioDAO().guardarUsuario(usuario);

                        //Crear la rutina inicial
                        RutinaEntity rutina = RutinaMapper.toEntity(RutinasRepository.getRutinaInicial());
                        rutina.setId_usuario(userId);
                        List<SemanaEntity> semanas = SemanaMapper.toEntities(RutinasRepository.getSemanasIniciales());
                        List<DiaEntity> dias = DiaMapper.toEntities(RutinasRepository.getDiasIniciales());
                        List<EjercicioEntity> ejercicios = EjercicioMapper.toEntities(RutinasRepository.getEjerciciosIniciales());

                        getInstance(context).rutinaDAO().guardarRutinaCompleta(rutina,semanas,dias,ejercicios);
                    }
                });
            }
        }).build();
    }
}
