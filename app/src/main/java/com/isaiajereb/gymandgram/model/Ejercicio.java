package com.isaiajereb.gymandgram.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Optional;
import java.util.UUID;

public class Ejercicio implements Parcelable {
    private UUID id;
    private String nombre;
    private Integer posicion;
    private Optional<Integer> series;
    private Optional<Integer> repeticiones;
    private Optional<Double> peso;
    private Optional<Double> tiempo_cantidad;
    private Optional<UnidadTiempo> tiempo_unidad;
    private String observaciones;

    private UUID id_dia;

    public Ejercicio() {
        this.series = Optional.empty();
        this.repeticiones = Optional.empty();
        this.peso = Optional.empty();
        this.tiempo_cantidad = Optional.empty();
        this.tiempo_unidad = Optional.empty();
    }

    private Ejercicio(Parcel in){
        this.readFromParcel(in);
    }

    public static final Parcelable.Creator<Ejercicio> CREATOR=
            new Parcelable.Creator<Ejercicio>(){
        public Ejercicio createFromParcel(Parcel in){
            return new Ejercicio(in);
    }
        public Ejercicio[] newArray(int size){
            return new Ejercicio[size];
        }
    };

    public Ejercicio(UUID id, String nombre, Integer posicion, Optional<Integer> series, Optional<Integer> repeticiones, Optional<Double> peso, Optional<Double> tiempo_cantidad, Optional<UnidadTiempo> tiempo_unidad, String observaciones, UUID id_dia) {
        this.id = id;
        this.nombre = nombre;
        this.posicion = posicion;
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

    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeSerializable(id);
        parcel.writeString(nombre);
        parcel.writeValue(series);
        parcel.writeValue(repeticiones);
        parcel.writeValue(peso);
        parcel.writeValue(tiempo_cantidad);
        parcel.writeValue(tiempo_unidad);
        parcel.writeString(observaciones);

        parcel.writeSerializable(id_dia);
    }

    private void readFromParcel(Parcel in){
        id= (UUID) in.readSerializable();
        nombre = in.readString();
        series = (Optional<Integer>) in.readValue(Optional.class.getClassLoader());
        repeticiones = (Optional<Integer>) in.readValue(Optional.class.getClassLoader());
        peso = (Optional<Double>) in.readValue(Optional.class.getClassLoader());
        tiempo_cantidad = (Optional<Double>) in.readValue(Optional.class.getClassLoader());
        tiempo_unidad = (Optional<UnidadTiempo>) in.readValue(Optional.class.getClassLoader());

        observaciones= in.readString();
        id_dia = (UUID) in.readSerializable();
    }

}
