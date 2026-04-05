package com.eduPlazas.eduPlazas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Tutor {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private String nombre;
private String apellidos;
private String dniNie;
private String relacionConMenor;
private String telefono;
private String email;
private String situacionLaboral;

public Tutor() {
}

public Tutor(Long id, String nombre, String apellidos, String dniNie, String relacionConMenor,
String telefono, String email, String situacionLaboral) {
this.id = id;
this.nombre = nombre;
this.apellidos = apellidos;
this.dniNie = dniNie;
this.relacionConMenor = relacionConMenor;
this.telefono = telefono;
this.email = email;
this.situacionLaboral = situacionLaboral;
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

public String getDniNie() {
return dniNie;
}

public void setDniNie(String dniNie) {
this.dniNie = dniNie;
}

public String getRelacionConMenor() {
return relacionConMenor;
}

public void setRelacionConMenor(String relacionConMenor) {
this.relacionConMenor = relacionConMenor;
}

public String getTelefono() {
return telefono;
}

public void setTelefono(String telefono) {
this.telefono = telefono;
}

public String getEmail() {
return email;
}

public void setEmail(String email) {
this.email = email;
}

public String getSituacionLaboral() {
return situacionLaboral;
}

public void setSituacionLaboral(String situacionLaboral) {
this.situacionLaboral = situacionLaboral;
}
}