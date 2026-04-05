package com.eduPlazas.eduPlazas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Centro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String direccion;
    private String ciudad;
    private String email;
    private String telefono;
    private String web;

    public Centro(String nombre, String direccion, String ciudad, String email, String telefono, String web) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.email = email;
        this.telefono = telefono;
        this.web = web;
    }

    // Getters and setters
}