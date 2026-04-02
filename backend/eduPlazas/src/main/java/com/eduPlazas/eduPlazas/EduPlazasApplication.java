package com.eduPlazas.eduPlazas;

import com.eduPlazas.eduPlazas.model.DocumentoAdjunto;
import com.eduPlazas.eduPlazas.model.Menor;
import com.eduPlazas.eduPlazas.model.Solicitud;
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

Menor menor1 = new Menor(null, "Lucia", "Garcia", "2021-05-10");
Solicitud solicitud1 = new Solicitud(null, "Laura Vicente", "Pendiente", menor1);

DocumentoAdjunto doc1 = new DocumentoAdjunto(null, "empadronamiento.pdf", "Empadronamiento");
doc1.setSolicitud(solicitud1);

List<DocumentoAdjunto> documentos1 = new ArrayList<>();
documentos1.add(doc1);
solicitud1.setDocumentos(documentos1);

Menor menor2 = new Menor(null, "Pablo", "Sanchez", "2020-11-03");
Solicitud solicitud2 = new Solicitud(null, "Daniel Sanchez", "Aceptada", menor2);

DocumentoAdjunto doc2 = new DocumentoAdjunto(null, "renta.pdf", "Renta");
doc2.setSolicitud(solicitud2);

List<DocumentoAdjunto> documentos2 = new ArrayList<>();
documentos2.add(doc2);
solicitud2.setDocumentos(documentos2);

solicitudRepository.save(solicitud1);
solicitudRepository.save(solicitud2);
}
};
}
}