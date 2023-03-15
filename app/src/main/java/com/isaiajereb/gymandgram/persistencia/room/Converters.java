package com.isaiajereb.gymandgram.persistencia.room;

import androidx.room.TypeConverter;

import com.isaiajereb.gymandgram.model.DiaSemana;
import com.isaiajereb.gymandgram.model.Genero;
import com.isaiajereb.gymandgram.model.UnidadTiempo;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Converters {
    @TypeConverter
    public static String stringFromUUID(UUID id){
        return id.toString();
    }

    @TypeConverter
    public static UUID UUIDFromString(String s){
        return UUID.fromString(s);
    }

    @TypeConverter
    public static DiaSemana diaSemanaFromString(String s){
        for (DiaSemana d : DiaSemana.values()) {
            if (d.toString().equalsIgnoreCase(s)) {
                return d;
            }
        }
        return null;
    }

    @TypeConverter
    public static String stringFromDiaSemana(DiaSemana dia){
        return dia.toString();
    }

    @TypeConverter
    public static LocalTime localTimeFromString(String s){
        return LocalTime.parse(s, DateTimeFormatter.ofPattern("HH:mm"));
    }

    @TypeConverter
    public static String stringFromLocalTime(LocalTime l){
        return l.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    @TypeConverter
    public static UnidadTiempo unidadTiempoFromString(String s){
        for(UnidadTiempo u : UnidadTiempo.values()){
            if(u.toString().equalsIgnoreCase(s)){
                return u;
            }
        }
        return null;
    }

    @TypeConverter
    public static String stringFromUnidadTiempo(UnidadTiempo unidad){
        return unidad.toString();
    }

    @TypeConverter
    public static Integer intFromBoolean(Boolean b){
        return b ? 1 : 0;
    }

    @TypeConverter
    public static Boolean booleanFromInt(Integer i){
        return i == 1 ? true : false;
    }

    @TypeConverter
    public static Genero generoFromString(String s){
        for(Genero g : Genero.values()){
            if(g.toString().equalsIgnoreCase(s)){
                return g;
            }
        }
        return null;
    }

    @TypeConverter
    public static String stringFromGenero(Genero genero){
        return genero.toString();
    }
}
