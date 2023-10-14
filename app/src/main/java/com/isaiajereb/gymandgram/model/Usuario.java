package com.isaiajereb.gymandgram.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Optional;
import java.util.UUID;

public class Usuario {
    private UUID id;
    private String nombre;
    private String mail;
    private Genero genero;
    private Integer edad;
    private String password;
    private Bitmap fotoPerfil;

    public Usuario() {
    }

    public Usuario(UUID id, String nombre, String mail, Genero genero, Integer edad, String password, Bitmap fotoPerfil) {
        this.id = id;
        this.nombre = nombre;
        this.mail = mail;
        this.genero = genero;
        this.edad = edad;
        this.password = password;
        this.fotoPerfil = fotoPerfil;
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

    public Bitmap getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(Bitmap fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
}
