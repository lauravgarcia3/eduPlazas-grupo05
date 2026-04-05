package com.eduPlazas.eduPlazas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class DomicilioFamiliar {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private String direccionCompleta;
private String ciudad;
private String codigoPostal;
private String provincia;

public DomicilioFamiliar() {
}

public DomicilioFamiliar(Long id, String direccionCompleta, String ciudad, String codigoPostal, String provincia) {
this.id = id;
this.direccionCompleta = direccionCompleta;
this.ciudad = ciudad;
this.codigoPostal = codigoPostal;
this.provincia = provincia;
}

public Long getId() {
return id;
}

public void setId(Long id) {
this.id = id;
}

public String getDireccionCompleta() {
return direccionCompleta;
}

public void setDireccionCompleta(String direccionCompleta) {
this.direccionCompleta = direccionCompleta;
}

public String getCiudad() {
return ciudad;
}

public void setCiudad(String ciudad) {
this.ciudad = ciudad;
}

public String getCodigoPostal() {
return codigoPostal;
}

public void setCodigoPostal(String codigoPostal) {
this.codigoPostal = codigoPostal;
}

public String getProvincia() {
return provincia;
}

public void setProvincia(String provincia) {
this.provincia = provincia;
}
}