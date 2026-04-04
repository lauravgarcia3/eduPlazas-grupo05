package com.eduPlazas.eduPlazas.model;


import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Convocatoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private Integer plazasTotales;

    //Constructores
    public Convocatoria() {
    }

    public Convocatoria(String nombre, LocalDate fechaInicio, LocalDate fechaFin, Integer plazasTotales) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.plazasTotales = plazasTotales;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public Integer getPlazasTotales() { return plazasTotales; }
    public void setPlazasTotales(Integer plazasTotales) { this.plazasTotales = plazasTotales; }
}
