package com.eduPlazas.eduPlazas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Puntuacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double totalPuntos = 0.0;

    private Double puntosHermanos = 0.0;
    private Double puntosProximidad = 0.0;
    private Double puntosTrabajoCentro = 0.0;
    private Double puntosFamiliaNumerosa = 0.0;
    private Double puntosFamiliaMonoparental = 0.0;
    private Double puntosDiscapacidad = 0.0;
    private Double puntosRenta = 0.0;

    private LocalDateTime fechaCalculo;

    @OneToOne
    @JoinColumn(name = "solicitud_id", nullable = false, unique = true)
    private Solicitud solicitud;

    public Puntuacion() {
        this.fechaCalculo = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Double getTotalPuntos() {
        return totalPuntos;
    }

    public void setTotalPuntos(Double totalPuntos) {
        this.totalPuntos = totalPuntos;
    }

    public Double getPuntosHermanos() {
        return puntosHermanos;
    }

    public void setPuntosHermanos(Double puntosHermanos) {
        this.puntosHermanos = puntosHermanos;
    }

    public Double getPuntosProximidad() {
        return puntosProximidad;
    }

    public void setPuntosProximidad(Double puntosProximidad) {
        this.puntosProximidad = puntosProximidad;
    }

    public Double getPuntosTrabajoCentro() {
        return puntosTrabajoCentro;
    }

    public void setPuntosTrabajoCentro(Double puntosTrabajoCentro) {
        this.puntosTrabajoCentro = puntosTrabajoCentro;
    }

    public Double getPuntosFamiliaNumerosa() {
        return puntosFamiliaNumerosa;
    }

    public void setPuntosFamiliaNumerosa(Double puntosFamiliaNumerosa) {
        this.puntosFamiliaNumerosa = puntosFamiliaNumerosa;
    }

    public Double getPuntosFamiliaMonoparental() {
        return puntosFamiliaMonoparental;
    }

    public void setPuntosFamiliaMonoparental(Double puntosFamiliaMonoparental) {
        this.puntosFamiliaMonoparental = puntosFamiliaMonoparental;
    }

    public Double getPuntosDiscapacidad() {
        return puntosDiscapacidad;
    }

    public void setPuntosDiscapacidad(Double puntosDiscapacidad) {
        this.puntosDiscapacidad = puntosDiscapacidad;
    }

    public Double getPuntosRenta() {
        return puntosRenta;
    }

    public void setPuntosRenta(Double puntosRenta) {
        this.puntosRenta = puntosRenta;
    }

    public LocalDateTime getFechaCalculo() {
        return fechaCalculo;
    }

    public void setFechaCalculo(LocalDateTime fechaCalculo) {
        this.fechaCalculo = fechaCalculo;
    }

    public Solicitud getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }
}