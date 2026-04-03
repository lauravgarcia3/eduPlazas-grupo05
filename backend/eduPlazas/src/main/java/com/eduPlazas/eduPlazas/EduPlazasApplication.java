package com.eduPlazas.eduPlazas;

import com.eduPlazas.eduPlazas.model.*;
import com.eduPlazas.eduPlazas.repository.SolicitudRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class EduPlazasApplication {

public static void main(String[] args) {
SpringApplication.run(EduPlazasApplication.class, args);
}

@Bean
CommandLineRunner initData(SolicitudRepository solicitudRepository) {
return args -> {
if (solicitudRepository.count() == 0) {

// ========================
// SOLICITUD 1 - PENDIENTE
// ========================

Menor menor1 = new Menor(null, "Lucia", "Garcia", "2021-05-10", "Madrid", "F");

Tutor tutor1_1 = new Tutor(null, "Laura", "Vicente", "12345678A",
"Madre", "600123123", "laura@email.com", "Trabajando");

Tutor tutor2_1 = new Tutor(null, "Carlos", "Garcia", "87654321B",
"Padre", "600999999", "carlos@email.com", "Desempleado");

DomicilioFamiliar domicilio1 = new DomicilioFamiliar(null,
"Calle Mayor 1", "Madrid", "28001", "Madrid");

Solicitud solicitud1 = new Solicitud();
solicitud1.setNombreSolicitante("Laura Vicente");
solicitud1.setEstado("Pendiente");

// TODO LOGIN:
// Este usuario es temporal. Sustituir por el usuario autenticado real
solicitud1.setUsuario("familia1");

solicitud1.setMenor(menor1);
solicitud1.setTutor1(tutor1_1);
solicitud1.setTutor2(tutor2_1);
solicitud1.setDomicilioFamiliar(domicilio1);

solicitud1.setCentroPreferencia("Colegio Público Madrid");
solicitud1.setCursoSolicitado("Infantil 3 años");

solicitud1.setDeclaracionVeracidad(true);
solicitud1.setAutorizacionProteccionDatos(true);

DocumentoAdjunto doc1 = new DocumentoAdjunto(null, "empadronamiento.pdf", "Empadronamiento");
doc1.setSolicitud(solicitud1);

List<DocumentoAdjunto> documentos1 = new ArrayList<>();
documentos1.add(doc1);
solicitud1.setDocumentos(documentos1);

// ========================
// SOLICITUD 2 - ACEPTADA
// ========================

Menor menor2 = new Menor(null, "Pablo", "Sanchez", "2020-11-03", "Madrid", "M");

Tutor tutor1_2 = new Tutor(null, "Daniel", "Sanchez", "22222222C",
"Padre", "611111111", "daniel@email.com", "Trabajando");

DomicilioFamiliar domicilio2 = new DomicilioFamiliar(null,
"Calle Sol 5", "Madrid", "28002", "Madrid");

Solicitud solicitud2 = new Solicitud();
solicitud2.setNombreSolicitante("Daniel Sanchez");
solicitud2.setEstado("Aceptada");

// TODO LOGIN:
solicitud2.setUsuario("familia2");

solicitud2.setMenor(menor2);
solicitud2.setTutor1(tutor1_2);
solicitud2.setDomicilioFamiliar(domicilio2);

solicitud2.setCentroPreferencia("Colegio San Juan");
solicitud2.setCursoSolicitado("Infantil 4 años");

solicitud2.setDeclaracionVeracidad(true);
solicitud2.setAutorizacionProteccionDatos(true);

DocumentoAdjunto doc2 = new DocumentoAdjunto(null, "renta.pdf", "Renta");
doc2.setSolicitud(solicitud2);

List<DocumentoAdjunto> documentos2 = new ArrayList<>();
documentos2.add(doc2);
solicitud2.setDocumentos(documentos2);

// GUARDAR
solicitudRepository.save(solicitud1);
solicitudRepository.save(solicitud2);
}
};
}
}
