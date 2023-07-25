package com.isaiajereb.gymandgram.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.UUID;

public class Rutina implements Parcelable {
    private UUID id;
    private String nombre;
    private Boolean actual;

    private UUID id_usuario;

    public Rutina() {
    }

    public Rutina(Parcel in){
        this.readFromParcel(in);
    }

    public static final Parcelable.Creator<Rutina> CREATOR =
            new Parcelable.Creator<Rutina>(){
                @Override
                public Rutina createFromParcel(Parcel source) {
                    return new Rutina(source);
                }
                @Override
                public Rutina[] newArray(int size) {
                    return new Rutina[size];
                }
            };

    public Rutina(UUID id, String nombre, Boolean actual, UUID id_usuario) {
        this.id = id;
        this.nombre = nombre;
        this.actual = actual;
        this.id_usuario = id_usuario;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getActual() {
        return actual;
    }

    public void setActual(Boolean actual) {
        this.actual = actual;
    }

    public UUID getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(UUID id_usuario) {
        this.id_usuario = id_usuario;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeSerializable(id);
        parcel.writeString(nombre);
        int actualAux = actual ? 1 : 0;
        parcel.writeInt(actualAux);
        parcel.writeSerializable(id_usuario);
    }

    private void readFromParcel(Parcel in){
        id = (UUID) in.readSerializable();
        nombre = in.readString();
        int actualAux = in.readInt();
        actual = (actualAux == 1);
        id_usuario = (UUID) in.readSerializable();
    }
}
