package com.isaiajereb.gymandgram.persistencia.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.isaiajereb.gymandgram.model.Rutina;

import java.util.UUID;

@Entity(tableName = "semana",
        foreignKeys = {@ForeignKey(entity = RutinaEntity.class, parentColumns = "id", childColumns = "id_rutina", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)})
public class SemanaEntity {
    @PrimaryKey
    @NonNull
    private UUID id;

    private Integer numero;

    private UUID id_rutina;

    public SemanaEntity(@NonNull UUID id, Integer numero, UUID id_rutina) {
        this.id = id;
        this.numero = numero;
        this.id_rutina = id_rutina;
    }

    @NonNull
    public UUID getId() {
        return id;
    }

    public void setId(@NonNull UUID id) {
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
}
