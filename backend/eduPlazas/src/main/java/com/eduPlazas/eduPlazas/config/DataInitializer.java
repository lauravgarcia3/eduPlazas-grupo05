package com.eduPlazas.eduPlazas.config;

import com.eduPlazas.eduPlazas.model.*;
import com.eduPlazas.eduPlazas.repository.UsuarioRepository;
import com.eduPlazas.eduPlazas.repository.ConvocatoriaRepository;
import com.eduPlazas.eduPlazas.repository.SolicitudRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
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
            if (usuarioRepository.findByEmail("admin@eduplazas.com").isEmpty()) {
                Usuario admin = new Usuario();
                Usuario solicitante = new Usuario();
                admin.setEmail("admin@eduplazas.com");
                solicitante.setEmail("solicitante@eduplazas.com");
                
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setNombreCompleto("Administrador Principal");
                admin.setRol("ROLE_ADMIN");
                
                solicitante.setPassword(passwordEncoder.encode("solicitante123"));
                solicitante.setNombreCompleto("Solicitante Ejemplo");
                solicitante.setRol("ROLE_SOLICITANTE");

                usuarioRepository.save(admin);
                usuarioRepository.save(solicitante);
                System.out.println("✅ Usuario ADMIN creado por defecto: admin@eduplazas.com / admin123");
                System.out.println("✅ Usuario SOLICITANTE creado por defecto: solicitante@eduplazas.com / solicitante123");
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

                convocatoriaRepository.saveAll(List.of(conv2024, conv2025));
                System.out.println("Convocatorias históricas (2024 y 2025) creadas.");
            }

            // ==========================================
            // 3. POBLAR SOLICITUDES
            // ==========================================
            if (solicitudRepository.count() == 0) {

                // SOLICITUD 1 - PENDIENTE
                Menor menor1 = new Menor(null, "Lucia", "Garcia", "2021-05-10", "Madrid", "F");
                Tutor tutor1_1 = new Tutor(null, "Laura", "Vicente", "12345678A", "Madre", "600123123", "laura@email.com", "Trabajando");
                Tutor tutor2_1 = new Tutor(null, "Carlos", "Garcia", "87654321B", "Padre", "600999999", "carlos@email.com", "Desempleado");
                DomicilioFamiliar domicilio1 = new DomicilioFamiliar(null, "Calle Mayor 1", "Madrid", "28001", "Madrid");

                Solicitud solicitud1 = new Solicitud();
                solicitud1.setNombreSolicitante("Laura Vicente");
                solicitud1.setEstado("Pendiente");
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

                // SOLICITUD 2 - ACEPTADA
                Menor menor2 = new Menor(null, "Pablo", "Sanchez", "2020-11-03", "Madrid", "M");
                Tutor tutor1_2 = new Tutor(null, "Daniel", "Sanchez", "22222222C", "Padre", "611111111", "daniel@email.com", "Trabajando");
                DomicilioFamiliar domicilio2 = new DomicilioFamiliar(null, "Calle Sol 5", "Madrid", "28002", "Madrid");

                Solicitud solicitud2 = new Solicitud();
                solicitud2.setNombreSolicitante("Daniel Sanchez");
                solicitud2.setEstado("Aceptada");
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

                solicitudRepository.save(solicitud1);
                solicitudRepository.save(solicitud2);
                System.out.println("Solicitudes de prueba creadas.");
            }
        };
    }
}