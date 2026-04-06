package com.eduPlazas.eduPlazas.config;

import com.eduPlazas.eduPlazas.model.*;
import com.eduPlazas.eduPlazas.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, 
                                      ConvocatoriaRepository convocatoriaRepository, SolicitudRepository solicitudRepository) {
        return args -> {
            
            // ==========================================
            // 1. POBLAR USUARIOS
            // ==========================================
           // ADMIN
            if (usuarioRepository.findByEmail("admin@eduplazas.com").isEmpty()) {
                Usuario admin = new Usuario();
<<<<<<< HEAD
                Usuario solicitante = new Usuario();
		Usuario centro = new Usuario();
                admin.setEmail("admin@eduplazas.com");
                solicitante.setEmail("solicitante@eduplazas.com");
		centro.setEmail("centro@eduplazas.com");
                
=======
                admin.setEmail("admin@eduplazas.com");
>>>>>>> main
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setNombreCompleto("Administrador Principal");
                admin.setRol("ROLE_ADMIN");
                usuarioRepository.save(admin);
                System.out.println("Usuario ADMIN creado.");
            }

            // SOLICITANTE
            if (usuarioRepository.findByEmail("solicitante@eduplazas.com").isEmpty()) {
                Usuario solicitante = new Usuario();
                solicitante.setEmail("solicitante@eduplazas.com");
                solicitante.setPassword(passwordEncoder.encode("solicitante123"));
                solicitante.setNombreCompleto("Solicitante Ejemplo");
                solicitante.setRol("ROLE_SOLICITANTE");
<<<<<<< HEAD

		centro.setPassword(passwordEncoder.encode("centro123"));
		centro.setNombreCompleto("Centro prueba");
		centro.setRol("ROLE_CENTRO");

                usuarioRepository.save(admin);
=======
>>>>>>> main
                usuarioRepository.save(solicitante);
                System.out.println("Usuario SOLICITANTE creado.");
            }

            // CENTRO
            if (usuarioRepository.findByEmail("centro@eduplazas.com").isEmpty()) {
                Usuario centroUser = new Usuario();
                centroUser.setEmail("centro@eduplazas.com");
                centroUser.setPassword(passwordEncoder.encode("centro123"));
                centroUser.setNombreCompleto("CEIP San Francisco - Gestión");
                centroUser.setRol("ROLE_CENTRO");
                usuarioRepository.save(centroUser);
                System.out.println("Usuario CENTRO creado.");
            }

            // ==========================================
            // 2. POBLAR CONVOCATORIAS
            // ==========================================
            if (convocatoriaRepository.count() == 0) {
                Convocatoria conv2024 = new Convocatoria();
                conv2024.setNombre("Educación Infantil 2024-2025");
                conv2024.setFechaInicio(LocalDate.of(2024, 4, 1));
                conv2024.setFechaFin(LocalDate.of(2024, 6, 28));
                conv2024.setEstado("CERRADA");
                conv2024.setNumeroPlazas(350);
                conv2024.setTipo("Educación Infantil");
                conv2024.setAnioAcademico("2024-2025");
                conv2024.setModalidad("Presencial");
                conv2024.setNombreCentro("Múltiples Centros");
                conv2024.setDescripcion("Convocatoria histórica del curso 2024-2025.");

                Convocatoria conv2025 = new Convocatoria();
                conv2025.setNombre("Educación Infantil 2025-2026");
                conv2025.setFechaInicio(LocalDate.of(2025, 3, 3));
                conv2025.setFechaFin(LocalDate.of(2025, 6, 13));
                conv2025.setEstado("CERRADA");
                conv2025.setNumeroPlazas(350);
                conv2025.setTipo("Educación Infantil");
                conv2025.setAnioAcademico("2025-2026");
                conv2025.setModalidad("Presencial");
                conv2025.setNombreCentro("Múltiples Centros");
                conv2025.setDescripcion("Convocatoria histórica del curso 2025-2026.");

                Convocatoria conv2026 = new Convocatoria();
                conv2026.setNombre("Educación Infantil 2026-2027");
                conv2026.setFechaInicio(LocalDate.of(2026, 3, 1));
                conv2026.setFechaFin(LocalDate.of(2026, 5, 31));
                conv2026.setEstado("ACTIVA");
                conv2026.setNumeroPlazas(400);
                conv2026.setTipo("Educación Infantil");
                conv2026.setAnioAcademico("2026-2027");
                conv2026.setModalidad("Presencial");
                conv2026.setNombreCentro("Múltiples Centros");
                conv2026.setDescripcion("Convocatoria abierta para el curso escolar 2026-2027.");

                convocatoriaRepository.saveAll(List.of(conv2024, conv2025, conv2026));
                System.out.println("Convocatorias históricas (2024, 2025 y 2026) creadas.");
            }

            // ==========================================
            // 3. POBLAR SOLICITUDES
            // ==========================================
            if (solicitudRepository.count() == 0) {

                // Recuperamos al solicitante que se acaba de crear arriba
                Usuario usuarioSolicitante = usuarioRepository.findByEmail("solicitante@eduplazas.com").orElse(null);

                // SOLICITUD 1 - PENDIENTE
                Menor menor1 = new Menor(null, "Lucia", "Garcia", "2021-05-10", "Madrid", "F");
                Tutor tutor1_1 = new Tutor(null, "Laura", "Vicente", "12345678A", "Madre", "600123123", "laura@email.com", "Trabajando");
                Tutor tutor2_1 = new Tutor(null, "Carlos", "Garcia", "87654321B", "Padre", "600999999", "carlos@email.com", "Desempleado");
                DomicilioFamiliar domicilio1 = new DomicilioFamiliar(null, "Calle Mayor 1", "Madrid", "28001", "Madrid");

                Solicitud solicitud1 = new Solicitud();
                solicitud1.setNombreSolicitante("Laura Vicente");
                solicitud1.setEstado("Pendiente");
                solicitud1.setUsuario(usuarioSolicitante); // Asignamos el usuario recuperado
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

                // SOLICITUD 2 - ACEPTADA
                Menor menor2 = new Menor(null, "Pablo", "Sanchez", "2020-11-03", "Madrid", "M");
                Tutor tutor1_2 = new Tutor(null, "Daniel", "Sanchez", "22222222C", "Padre", "611111111", "daniel@email.com", "Trabajando");
                DomicilioFamiliar domicilio2 = new DomicilioFamiliar(null, "Calle Sol 5", "Madrid", "28002", "Madrid");

                Solicitud solicitud2 = new Solicitud();
                solicitud2.setNombreSolicitante("Daniel Sanchez");
                solicitud2.setEstado("Aceptada");
                solicitud2.setUsuario(usuarioSolicitante); // Asignamos el usuario recuperado
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

                solicitudRepository.save(solicitud1);
                solicitudRepository.save(solicitud2);
                System.out.println("Solicitudes de prueba creadas.");
            }
        };
    }

    @Bean
    public CommandLineRunner initializeCenters(CentroRepository centroRepository) {
        return args -> {
            List<Centro> centros = Arrays.asList(
                new Centro("CEIP San Francisco de Asís", "Calle de la Educación, 123", "28001 Madrid", "info@ceipsanfrancisco.edu.es", "+34 91 234 5678", "www.ceipsanfrancisco.edu.es"),
                new Centro("CEIP Los Almendros", "Avenida de los Almendros, 45", "28002 Madrid", "contacto@ceipalmendros.edu.es", "+34 91 345 6789", "www.ceipalmendros.edu.es"),
                new Centro("CEIP El Prado", "Calle del Prado, 67", "28003 Madrid", "info@ceipelprado.edu.es", "+34 91 456 7890", "www.ceipelprado.edu.es"),
                new Centro("CEIP Las Rosas", "Plaza de las Rosas, 12", "28004 Madrid", "contacto@ceiplasrosas.edu.es", "+34 91 567 8901", "www.ceiplasrosas.edu.es"),
                new Centro("CEIP La Colina", "Camino de la Colina, 89", "28005 Madrid", "info@ceiplacolina.edu.es", "+34 91 678 9012", "www.ceiplacolina.edu.es"),
                new Centro("CEIP Los Pinos", "Calle de los Pinos, 34", "28006 Madrid", "contacto@ceiplospinos.edu.es", "+34 91 789 0123", "www.ceiplospinos.edu.es"),
                new Centro("CEIP El Olivo", "Avenida del Olivo, 56", "28007 Madrid", "info@ceipelolivo.edu.es", "+34 91 890 1234", "www.ceipelolivo.edu.es"),
                new Centro("CEIP La Fuente", "Plaza de la Fuente, 78", "28008 Madrid", "contacto@ceiplafuente.edu.es", "+34 91 901 2345", "www.ceiplafuente.edu.es"),
                new Centro("CEIP El Parque", "Calle del Parque, 90", "28009 Madrid", "info@ceipelparque.edu.es", "+34 91 012 3456", "www.ceipelparque.edu.es"),
                new Centro("CEIP Las Estrellas", "Avenida de las Estrellas, 23", "28010 Madrid", "contacto@ceiplasestrellas.edu.es", "+34 91 123 4567", "www.ceiplasestrellas.edu.es")
            );

            centroRepository.saveAll(centros);
        };
    }
}