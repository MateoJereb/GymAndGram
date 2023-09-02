package com.isaiajereb.gymandgram.model;

public enum Genero {
    Masculino("Masculino"),
    Femenino("Femenino"),
    Otro("Otro");

    private String nombre;

    Genero(String nombre){
        this.nombre=nombre;
    }
}
