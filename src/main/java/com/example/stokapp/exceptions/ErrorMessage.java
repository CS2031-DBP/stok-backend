package com.example.stokapp.exceptions;

public class ErrorMessage {

    String nombre;
    String code;

    public ErrorMessage(String nombre, String code) {
        this.nombre = nombre;
        this.code = code;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
