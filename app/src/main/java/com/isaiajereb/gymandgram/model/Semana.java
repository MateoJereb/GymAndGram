package com.isaiajereb.gymandgram.model;

import java.util.UUID;

public class Semana {
    private UUID id;
    private Integer numero;

    private UUID id_rutina;

    public Semana() {
    }

    public Semana(UUID id, Integer numero, UUID id_rutina) {
        this.id = id;
        this.numero = numero;
        this.id_rutina = id_rutina;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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
