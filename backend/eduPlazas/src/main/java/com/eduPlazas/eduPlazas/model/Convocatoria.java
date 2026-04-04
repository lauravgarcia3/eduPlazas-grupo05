package com.eduPlazas.eduPlazas.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "convocatorias")
public class Convocatoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private LocalDate fechaFin;

    @Column(length = 1000)
    private String descripcion;

    private String tipo;          // Ej: Educación Infantil
    private String estado;        // Ej: ACTIVA, CERRADA, BORRADOR
    private String anioAcademico; // Ej: 2026-2027
    private String modalidad;     // Ej: Presencial

    // --- CONSTRUCTORES ---
    public Convocatoria() {
    }

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getAnioAcademico() { return anioAcademico; }
    public void setAnioAcademico(String anioAcademico) { this.anioAcademico = anioAcademico; }

    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }

  

    // Añadimos los campos simples para el centro, esto luego lo meteremos en una tabla aparte que haga referencia a esta convocatoria, pero por ahora lo dejamos así hasta que tire todo
    private String nombreCentro;
    private Integer numeroPlazas;

    // --- GETTERS Y SETTERS NUEVOS ---
    public String getNombreCentro() { return nombreCentro; }
    public void setNombreCentro(String nombreCentro) { this.nombreCentro = nombreCentro; }

    public Integer getNumeroPlazas() { return numeroPlazas; }
    public void setNumeroPlazas(Integer numeroPlazas) { this.numeroPlazas = numeroPlazas; }
}