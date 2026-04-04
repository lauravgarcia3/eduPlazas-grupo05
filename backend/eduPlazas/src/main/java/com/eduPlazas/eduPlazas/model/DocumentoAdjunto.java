package com.eduPlazas.eduPlazas.model;

package com.eduPlazas.eduPlazas.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class DocumentoAdjunto {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private String nombreArchivo;
private String tipoDocumento;

@ManyToOne
@JsonIgnore
@JoinColumn(name = "solicitud_id")
private Solicitud solicitud;

public DocumentoAdjunto() {
}

public DocumentoAdjunto(Long id, String nombreArchivo, String tipoDocumento) {
this.id = id;
this.nombreArchivo = nombreArchivo;
this.tipoDocumento = tipoDocumento;
}

public Long getId() {
return id;
}

public void setId(Long id) {
this.id = id;
}

public String getNombreArchivo() {
return nombreArchivo;
}

public void setNombreArchivo(String nombreArchivo) {
this.nombreArchivo = nombreArchivo;
}

public String getTipoDocumento() {
return tipoDocumento;
}

public void setTipoDocumento(String tipoDocumento) {
this.tipoDocumento = tipoDocumento;
}

public Solicitud getSolicitud() {
return solicitud;
}

public void setSolicitud(Solicitud solicitud) {
this.solicitud = solicitud;
}
}
public DocumentoAdjunto() {
}

public DocumentoAdjunto(Long id, String nombreArchivo, String tipoDocumento) {
this.id = id;
this.nombreArchivo = nombreArchivo;
this.tipoDocumento = tipoDocumento;
}

public Long getId() {
return id;
}

public void setId(Long id) {
this.id = id;
}

public String getNombreArchivo() {
return nombreArchivo;
}

public void setNombreArchivo(String nombreArchivo) {
this.nombreArchivo = nombreArchivo;
}

public String getTipoDocumento() {
return tipoDocumento;
}

public void setTipoDocumento(String tipoDocumento) {
this.tipoDocumento = tipoDocumento;
}

public Solicitud getSolicitud() {
return solicitud;
}

public void setSolicitud(Solicitud solicitud) {
this.solicitud = solicitud;
}
}
