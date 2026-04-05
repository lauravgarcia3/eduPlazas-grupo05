package com.eduPlazas.eduPlazas.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Column;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Solicitud {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private String nombreSolicitante;
private String estado;

private String centroPreferencia;
private String cursoSolicitado;

private Boolean declaracionVeracidad;
private Boolean autorizacionProteccionDatos;
private Boolean completada = false;

@OneToOne(cascade = CascadeType.ALL)
@JoinColumn(name = "menor_id")
private Menor menor;

@OneToOne(cascade = CascadeType.ALL)
@JoinColumn(name = "tutor1_id")
private Tutor tutor1;

@OneToOne(cascade = CascadeType.ALL)
@JoinColumn(name = "tutor2_id")
private Tutor tutor2;

@OneToOne(cascade = CascadeType.ALL)
@JoinColumn(name = "domicilio_id")
private DomicilioFamiliar domicilioFamiliar;

@OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL)
private List<DocumentoAdjunto> documentos = new ArrayList<>();

@ManyToOne
@JoinColumn(name = "usuario_id", nullable = false)
private Usuario usuario;

public Solicitud() {
}

public Solicitud(Long id, String nombreSolicitante, String estado, Usuario usuario) {
this.id = id;
this.nombreSolicitante = nombreSolicitante;
this.estado = estado;
this.usuario = usuario;
}

public Solicitud(Long id, String nombreSolicitante, String estado, Usuario usuario, Menor menor) {
    this.id = id;
    this.nombreSolicitante = nombreSolicitante;
    this.estado = estado;
    this.usuario = usuario;
    this.menor = menor;
}

public Solicitud(Long id, String nombreSolicitante, String estado, Usuario usuario,
                     Menor menor, Tutor tutor1, Tutor tutor2,
                     DomicilioFamiliar domicilioFamiliar,
                     String centroPreferencia, String cursoSolicitado,
                     Boolean declaracionVeracidad, Boolean autorizacionProteccionDatos) {
        this.id = id;
        this.nombreSolicitante = nombreSolicitante;
        this.estado = estado;
        this.usuario = usuario;
        this.menor = menor;
        this.tutor1 = tutor1;
        this.tutor2 = tutor2;
        this.domicilioFamiliar = domicilioFamiliar;
        this.centroPreferencia = centroPreferencia;
        this.cursoSolicitado = cursoSolicitado;
        this.declaracionVeracidad = declaracionVeracidad;
        this.autorizacionProteccionDatos = autorizacionProteccionDatos;
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

public Usuario getUsuario() {
return usuario;
}

public void setUsuario(Usuario usuario) {
this.usuario = usuario;
}

public String getCentroPreferencia() {
return centroPreferencia;
}

public void setCentroPreferencia(String centroPreferencia) {
this.centroPreferencia = centroPreferencia;
}

public String getCursoSolicitado() {
return cursoSolicitado;
}

public void setCursoSolicitado(String cursoSolicitado) {
this.cursoSolicitado = cursoSolicitado;
}

public Boolean getDeclaracionVeracidad() {
return declaracionVeracidad;
}

public void setDeclaracionVeracidad(Boolean declaracionVeracidad) {
this.declaracionVeracidad = declaracionVeracidad;
}

public Boolean getAutorizacionProteccionDatos() {
return autorizacionProteccionDatos;
}

public void setAutorizacionProteccionDatos(Boolean autorizacionProteccionDatos) {
this.autorizacionProteccionDatos = autorizacionProteccionDatos;
}

public Boolean getCompletada() {
return completada;
}

public void setCompletada(Boolean completada) {
this.completada = completada;
}

public Menor getMenor() {
return menor;
}

public void setMenor(Menor menor) {
this.menor = menor;
}

public Tutor getTutor1() {
return tutor1;
}

public void setTutor1(Tutor tutor1) {
this.tutor1 = tutor1;
}

public Tutor getTutor2() {
return tutor2;
}

public void setTutor2(Tutor tutor2) {
this.tutor2 = tutor2;
}

public DomicilioFamiliar getDomicilioFamiliar() {
return domicilioFamiliar;
}

public void setDomicilioFamiliar(DomicilioFamiliar domicilioFamiliar) {
this.domicilioFamiliar = domicilioFamiliar;
}

public List<DocumentoAdjunto> getDocumentos() {
return documentos;
}

public void setDocumentos(List<DocumentoAdjunto> documentos) {
this.documentos = documentos;
}
}