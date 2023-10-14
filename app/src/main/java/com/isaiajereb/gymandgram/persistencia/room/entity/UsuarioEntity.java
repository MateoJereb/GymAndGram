package com.isaiajereb.gymandgram.persistencia.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.isaiajereb.gymandgram.model.Genero;

import java.util.UUID;

@Entity(tableName = "usuario")
public class UsuarioEntity {
    @PrimaryKey
    @NonNull
    private UUID id;
    private String nombre;
    private String mail;
    private Genero genero;
    private Integer edad;
    private String password;
//    private byte[] fotoPerfil;
    public UsuarioEntity(@NonNull UUID id, String nombre, String mail, Genero genero, Integer edad, String password /*,byte[] fotoPerfil*/) {
        this.id = id;
        this.nombre = nombre;
        this.mail = mail;
        this.genero = genero;
        this.edad = edad;
        this.password = password;
//        this.fotoPerfil=fotoPerfil;
    }

    @NonNull
    public UUID getId() {
        return id;
    }

    public void setId(@NonNull UUID id) {
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

//    public byte[] getFotoPerfil() {
//        return fotoPerfil;
//    }
//
//    public void setFotoPerfil(byte[] fotoPerfil) {
//        this.fotoPerfil = fotoPerfil;
//    }
}
