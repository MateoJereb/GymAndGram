package com.isaiajereb.gymandgram.persistencia.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(tableName = "rutina",
        foreignKeys = {@ForeignKey(entity = UsuarioEntity.class, parentColumns = "id",childColumns = "id_usuario",onDelete = ForeignKey.CASCADE,onUpdate = ForeignKey.CASCADE)})
public class RutinaEntity {
    @PrimaryKey
    @NonNull
    private UUID id;
    private String nombre;
    private Boolean actual;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaUltimaModificacion;

    private UUID id_usuario;

    public RutinaEntity(@NonNull UUID id, String nombre, Boolean actual, LocalDateTime fechaCreacion, LocalDateTime fechaUltimaModificacion, UUID id_usuario) {
        this.id = id;
        this.nombre = nombre;
        this.actual = actual;
        this.fechaCreacion = fechaCreacion;
        this.fechaUltimaModificacion = fechaUltimaModificacion;
        this.id_usuario = id_usuario;
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

    public Boolean getActual() {
        return actual;
    }

    public void setActual(Boolean actual) {
        this.actual = actual;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaUltimaModificacion() {
        return fechaUltimaModificacion;
    }

    public void setFechaUltimaModificacion(LocalDateTime fechaUltimaModificacion) {
        this.fechaUltimaModificacion = fechaUltimaModificacion;
    }

    public UUID getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(UUID id_usuario) {
        this.id_usuario = id_usuario;
    }
}
