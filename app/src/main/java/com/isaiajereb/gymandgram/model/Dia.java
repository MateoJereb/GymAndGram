package com.isaiajereb.gymandgram.model;

import java.time.LocalTime;
import java.util.UUID;

public class Dia {
    private UUID id;
    private DiaSemana nombre;
    private LocalTime hora;

    private UUID id_semana;

    public Dia() {
    }

    public Dia(UUID id, DiaSemana nombre, LocalTime hora, UUID id_rutina) {
        this.id = id;
        this.nombre = nombre;
        this.hora = hora;
        this.id_semana = id_rutina;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public UUID getId_semana() {
        return id_semana;
    }

    public void setId_semana(UUID id_rutina) {
        this.id_semana = id_rutina;
    }
}
