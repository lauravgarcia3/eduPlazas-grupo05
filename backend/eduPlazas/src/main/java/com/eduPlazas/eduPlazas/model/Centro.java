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
    private String imagen;
    private Integer numPlazas = 0;

    public Centro() {}
    public Centro(String nombre, String direccion, String ciudad, String email, String telefono, String web, String imagen, Integer numPlazas) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.email = email;
        this.telefono = telefono;
        this.web = web;
        this.imagen = imagen;
        this.numPlazas = numPlazas;
    }

    public Long getId() {
return id;
}

public void setId(Long id) {
this.id = id;
}

public String getNombre() {
return nombre;
}

public void setNombre(String nombre) {
this.nombre = nombre;
}

public String getDireccion() {
return direccion;
}

public void setDireccion(String direccion) {
this.direccion = direccion;
}

public String getCiudad() {
return ciudad;
}

public void setCiudad(String ciudad) {
this.ciudad = ciudad;
}

public String getEmail() {
return email;
}

public void setEmail(String email) {
this.email = email;
}

public String getTelefono() {
return telefono;
}

public void setTelefono(String telefono) {
this.telefono = telefono;
}

public String getWeb() {
return web;
}

public void setWeb(String web) {
this.web = web;
}
public String getImagen() { 
return imagen; 
}
public void setImagen(String imagen) {
this.imagen = imagen; 
}
public Integer getNumPlazas() {
return numPlazas;
}
public void setNumPlazas(Integer numPlazas) {
this.numPlazas = numPlazas;
}
}