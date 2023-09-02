package com.isaiajereb.gymandgram.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Optional;
import java.util.UUID;

public class Usuario implements Parcelable {
    private UUID id;
    private String nombre;
    private String mail;
    private Genero genero;
    private Integer edad;
    private String password;

    public Usuario() {
    }

    public Usuario(UUID id, String nombre, String mail, Genero genero, Integer edad, String password) {
        this.id = id;
        this.nombre = nombre;
        this.mail = mail;
        this.genero = genero;
        this.edad = edad;
        this.password = password;
    }

    protected Usuario(Parcel in) {
        this.readFromParcel(in);
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeSerializable(id);
        parcel.writeString(nombre);
        parcel.writeString(mail);
        parcel.writeString(genero.name());
        parcel.writeInt(edad);
        parcel.writeString(password);
    }

    private void readFromParcel(Parcel in){
        id = (UUID) in.readSerializable();
        nombre = in.readString();
        mail = in.readString();
        genero = Genero.valueOf(in.readString());
        edad =  in.readInt();
        password = in.readString();
    }
}
