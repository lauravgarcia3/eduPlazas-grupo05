package com.eduPlazas.eduPlazas.model;

public class Menor {

private Long id;
private String nombre;
private String apellidos;
private String fechaNacimiento;

public Menor() {
}

public Menor(Long id, String nombre, String apellidos, String fechaNacimiento) {
this.id = id;
this.nombre = nombre;
this.apellidos = apellidos;
this.fechaNacimiento = fechaNacimiento;
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
}

