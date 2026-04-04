package com.eduPlazas.eduPlazas.config;

import com.eduPlazas.eduPlazas.model.Usuario;
import com.eduPlazas.eduPlazas.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Comprobamos si el admin ya existe para no duplicarlo
            if (usuarioRepository.findByEmail("admin@eduplazas.com").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setEmail("admin@eduplazas.com");
                // ¡Aquí está la magia! Encriptamos la contraseña "admin123" antes de guardarla
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setNombreCompleto("Administrador Principal");
                admin.setRol("ROLE_ADMIN");
                
                usuarioRepository.save(admin);
                System.out.println("✅ Usuario ADMIN creado por defecto: admin@eduplazas.com / admin123");
            }
        };
    }
}