package com.isaiajereb.gymandgram.persistencia.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.isaiajereb.gymandgram.model.DiaSemana;

import java.time.LocalTime;
import java.util.UUID;

@Entity(tableName = "dia",
        foreignKeys = {@ForeignKey(entity = RutinaEntity.class,parentColumns = "id",childColumns = "id_rutina",onDelete = ForeignKey.CASCADE,onUpdate = ForeignKey.CASCADE)})
public class DiaEntity {
    @PrimaryKey
    @NonNull
    private UUID id;
    private DiaSemana nombre;
    private LocalTime hora;

    private UUID id_rutina;

    public DiaEntity() {
    }

    public DiaEntity(@NonNull UUID id, DiaSemana nombre, LocalTime hora, UUID id_rutina) {
        this.id = id;
        this.nombre = nombre;
        this.hora = hora;
        this.id_rutina = id_rutina;
    }

    @NonNull
    public UUID getId() {
        return id;
    }

    public void setId(@NonNull UUID id) {
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

    public UUID getId_rutina() {
        return id_rutina;
    }

    public void setId_rutina(UUID id_rutina) {
        this.id_rutina = id_rutina;
    }
}
