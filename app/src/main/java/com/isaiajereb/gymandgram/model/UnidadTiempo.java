package com.isaiajereb.gymandgram.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public enum UnidadTiempo {
    Segundo("Seg"),
    Minuto("Min");

    private String unidad;
    UnidadTiempo(String unidad) {
        this.unidad=unidad;
    }

}
