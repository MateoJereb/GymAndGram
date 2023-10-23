package com.isaiajereb.gymandgram.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.UUID;

public class Semana implements Parcelable {
    private UUID id;
    private Integer numero;

    private UUID id_rutina;

    public Semana() {
    }

    public Semana(Parcel in) {this.readFromParcel(in);}

    public static final Parcelable.Creator<Semana> CREATOR =
            new Parcelable.Creator<Semana>(){
                @Override
                public Semana createFromParcel(Parcel source) {
                    return new Semana(source);
                }

                @Override
                public Semana[] newArray(int size) {
                    return new Semana[size];
                }
            };

    public Semana(UUID id, Integer numero, UUID id_rutina) {
        this.id = id;
        this.numero = numero;
        this.id_rutina = id_rutina;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public UUID getId_rutina() {
        return id_rutina;
    }

    public void setId_rutina(UUID id_rutina) {
        this.id_rutina = id_rutina;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeSerializable(id);
        dest.writeInt(numero);
        dest.writeSerializable(id_rutina);
    }

    private void readFromParcel(Parcel in){
        id = (UUID) in.readSerializable();
        numero = in.readInt();
        id_rutina = (UUID) in.readSerializable();
    }
}
