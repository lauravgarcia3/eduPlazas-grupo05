package com.eduPlazas.eduPlazas.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Solicitud {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private String nombreSolicitante;
private String estado;

@OneToOne(cascade = CascadeType.ALL)
@JoinColumn(name = "menor_id")
private Menor menor;

@OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL)
private List<DocumentoAdjunto> documentos = new ArrayList<>();

public Solicitud() {
}

public Solicitud(Long id, String nombreSolicitante, String estado) {
this.id = id;
this.nombreSolicitante = nombreSolicitante;
this.estado = estado;
}

public Solicitud(Long id, String nombreSolicitante, String estado, Menor menor) {
this.id = id;
this.nombreSolicitante = nombreSolicitante;
this.estado = estado;
this.menor = menor;
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

public Menor getMenor() {
return menor;
}

public void setMenor(Menor menor) {
this.menor = menor;
}

public List<DocumentoAdjunto> getDocumentos() {
return documentos;
}

public void setDocumentos(List<DocumentoAdjunto> documentos) {
this.documentos = documentos;
}
}
