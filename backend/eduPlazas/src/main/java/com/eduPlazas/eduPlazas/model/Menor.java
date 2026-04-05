package com.eduPlazas.eduPlazas.model;

import jakarta.persistence.*;

@Entity
public class Menor {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private String nombre;
private String apellidos;
private String fechaNacimiento;

// NUEVOS CAMPOS (mockup)
private String lugarNacimiento;
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