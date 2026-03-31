package com.eduPlazas.eduPlazas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Solicitud {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private String nombreSolicitante;
private String estado;

public Solicitud() {
}

public Solicitud(Long id, String nombreSolicitante, String estado) {
this.id = id;
this.nombreSolicitante = nombreSolicitante;
this.estado = estado;
}

public Long getId() {
return id;
}

public void setId(Long id) {
this.id = id;
}

public String getNombreSolicitante() {
return nombreSolicitante;
}

public void setNombreSolicitante(String nombreSolicitante) {
this.nombreSolicitante = nombreSolicitante;
}

public String getEstado() {
return estado;
}

public void setEstado(String estado) {
this.estado = estado;
}
}
