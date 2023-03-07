package com.isaiajereb.gymandgram.model;

import java.util.UUID;

public class Rutina {
    private UUID id;
    private String nombre;
    private Boolean actual;

    private UUID id_usuario;

    public Rutina() {
    }

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
}
