package com.eduPlazas.eduPlazas.config;

import com.eduPlazas.eduPlazas.model.Usuario;
import com.eduPlazas.eduPlazas.repository.UsuarioRepository;
import com.eduPlazas.eduPlazas.model.Convocatoria;
import com.eduPlazas.eduPlazas.repository.ConvocatoriaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder , ConvocatoriaRepository convocatoriaRepository) {
        return args -> {
            // Poblar usuarios por defecto: un ADMIN y un SOLICITANTE
            if (usuarioRepository.findByEmail("admin@eduplazas.com").isEmpty()) {
                Usuario admin = new Usuario();
                Usuario solicitante = new Usuario();
                admin.setEmail("admin@eduplazas.com");
                solicitante.setEmail("solicitante@eduplazas.com");
                //Encriptamos la contraseña "admin123  solicitante123" antes de guardarla
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setNombreCompleto("Administrador Principal");
                admin.setRol("ROLE_ADMIN");
                solicitante.setPassword(passwordEncoder.encode("solicitante123"));
                solicitante.setNombreCompleto("Solicitante Ejemplo");
                solicitante.setRol("ROLE_SOLICITANTE");

                usuarioRepository.save(admin);
                usuarioRepository.save(solicitante);
                System.out.println(" Usuario ADMIN creado por defecto: admin@eduplazas.com / admin123");
                System.out.println(" Usuario SOLICITANTE creado por defecto: solicitante@eduplazas.com / solicitante123");
            }
            //Poblar convocatorias históricas falseadas (2024-2025 y 2025-2026)
            if (convocatoriaRepository.count() == 0) {
                
                // Convocatoria 2024-2025
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

                // Convocatoria 2025-2026
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

                // Las guardamos de golpe
                convocatoriaRepository.saveAll(List.of(conv2024, conv2025));
                System.out.println("Convocatorias históricas (2024 y 2025) creadas por defecto.");
            }
        };
    }
}