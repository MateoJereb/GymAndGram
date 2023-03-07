package com.isaiajereb.gymandgram.model;

import java.util.Optional;
import java.util.UUID;

public class Ejercicio {
    private UUID id;
    private String nombre;
    private Optional<Integer> series;
    private Optional<Integer> repeticiones;
    private Optional<Double> peso;
    private Optional<Double> tiempo_cantidad;
    private Optional<UnidadTiempo> tiempo_unidad;
    private String observaciones;

    private UUID id_dia;

    public Ejercicio() {
    }

    public Ejercicio(UUID id, String nombre, Optional<Integer> series, Optional<Integer> repeticiones, Optional<Double> peso, Optional<Double> tiempo_cantidad, Optional<UnidadTiempo> tiempo_unidad, String observaciones, UUID id_dia) {
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

    public Optional<Integer> getSeries() {
        return series;
    }

    public void setSeries(Optional<Integer> series) {
        this.series = series;
    }

    public Optional<Integer> getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(Optional<Integer> repeticiones) {
        this.repeticiones = repeticiones;
    }

    public Optional<Double> getPeso() {
        return peso;
    }

    public void setPeso(Optional<Double> peso) {
        this.peso = peso;
    }

    public Optional<Double> getTiempo_cantidad() {
        return tiempo_cantidad;
    }

    public void setTiempo_cantidad(Optional<Double> tiempo_cantidad) {
        this.tiempo_cantidad = tiempo_cantidad;
    }

    public Optional<UnidadTiempo> getTiempo_unidad() {
        return tiempo_unidad;
    }

    public void setTiempo_unidad(Optional<UnidadTiempo> tiempo_unidad) {
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
