package com.isaiajereb.gymandgram.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalTime;
import java.util.UUID;

public class Dia implements Parcelable{
    private UUID id;
    private DiaSemana nombre;
    private LocalTime hora;

    private UUID id_semana;

    public Dia() {
    }

    public Dia(Parcel in){
        this.readFromParcel(in);
    }

    public static final Parcelable.Creator<Dia> CREATOR =
            new Parcelable.Creator<Dia>(){
                @Override
                public Dia createFromParcel(Parcel source) {
                    return new Dia(source);
                }

                @Override
                public Dia[] newArray(int size) {
                    return new Dia[size];
                }
            };

    public Dia(UUID id, DiaSemana nombre, LocalTime hora, UUID id_semana) {
        this.id = id;
        this.nombre = nombre;
        this.hora = hora;
        this.id_semana = id_semana;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public DiaSemana getNombre() {
        return nombre;
    }

    public void setNombre(DiaSemana nombre) {
        this.nombre = nombre;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public UUID getId_semana() {
        return id_semana;
    }

    public void setId_semana(UUID id_rutina) {
        this.id_semana = id_rutina;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeSerializable(id);
        dest.writeString(nombre.name());
        dest.writeSerializable(hora);
        dest.writeSerializable(id_semana);
    }

    private void readFromParcel(Parcel in){
        id = (UUID) in.readSerializable();
        nombre = DiaSemana.valueOf(in.readString());
        hora = (LocalTime) in.readSerializable();
        id_semana = (UUID) in.readSerializable();
    }
}
