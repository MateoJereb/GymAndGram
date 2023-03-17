package com.isaiajereb.gymandgram.persistencia.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.isaiajereb.gymandgram.model.UnidadTiempo;

import java.util.Optional;
import java.util.UUID;

@Entity(tableName = "ejercicio",
        foreignKeys = {@ForeignKey(entity = DiaEntity.class,parentColumns = "id",childColumns = "id_dia",onDelete = ForeignKey.CASCADE,onUpdate = ForeignKey.CASCADE)})
public class EjercicioEntity {
    @PrimaryKey
    @NonNull
    private UUID id;
    private String nombre;
    private Integer series;
    private Integer repeticiones;
    private Double peso;
    private Double tiempo_cantidad;
    private UnidadTiempo tiempo_unidad;
    private String observaciones;

    private UUID id_dia;

    public EjercicioEntity(@NonNull UUID id, String nombre, Integer series, Integer repeticiones, Double peso, Double tiempo_cantidad, UnidadTiempo tiempo_unidad, String observaciones, UUID id_dia) {
        this.id = id;
        this.nombre = nombre;
        this.series = series;
        this.repeticiones = repeticiones;
        this.peso = peso;
        this.tiempo_cantidad = tiempo_cantidad;
        this.tiempo_unidad = tiempo_unidad;
        this.observaciones = observaciones;
        this.id_dia = id_dia;
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

    public Integer getSeries() {
        return series;
    }

    public void setSeries(Integer series) {
        this.series = series;
    }

    public Integer getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(Integer repeticiones) {
        this.repeticiones = repeticiones;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Double getTiempo_cantidad() {
        return tiempo_cantidad;
    }

    public void setTiempo_cantidad(Double tiempo_cantidad) {
        this.tiempo_cantidad = tiempo_cantidad;
    }

    public UnidadTiempo getTiempo_unidad() {
        return tiempo_unidad;
    }

    public void setTiempo_unidad(UnidadTiempo tiempo_unidad) {
        this.tiempo_unidad = tiempo_unidad;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public UUID getId_dia() {
        return id_dia;
    }

    public void setId_dia(UUID id_dia) {
        this.id_dia = id_dia;
    }
}
