package com.eduPlazas.eduPlazas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Menor {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@NotBlank(message = "Debe cumplimentar el campo «Nombre del niño/a».")
private String nombre;

@NotBlank(message = "Debe cumplimentar el campo «Apellidos del niño/a».")
private String apellidos;

@NotBlank(message = "Debe cumplimentar el campo «Fecha de nacimiento».")
private String fechaNacimiento;

@NotBlank(message = "Debe cumplimentar el campo «Lugar de nacimiento».")
private String lugarNacimiento;

@NotBlank(message = "Debe seleccionar el campo «Sexo».")
private String sexo;

public Menor() {
}

public Menor(Long id, String nombre, String apellidos, String fechaNacimiento,
String lugarNacimiento, String sexo) {
this.id = id;
this.nombre = nombre;
this.apellidos = apellidos;
this.fechaNacimiento = fechaNacimiento;
this.lugarNacimiento = lugarNacimiento;
this.sexo = sexo;
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

public String getApellidos() {
return apellidos;
}

public void setApellidos(String apellidos) {
this.apellidos = apellidos;
}

public String getFechaNacimiento() {
return fechaNacimiento;
}

public void setFechaNacimiento(String fechaNacimiento) {
this.fechaNacimiento = fechaNacimiento;
}

public String getLugarNacimiento() {
return lugarNacimiento;
}

public void setLugarNacimiento(String lugarNacimiento) {
this.lugarNacimiento = lugarNacimiento;
}

public String getSexo() {
return sexo;
}

public void setSexo(String sexo) {
this.sexo = sexo;
}
}