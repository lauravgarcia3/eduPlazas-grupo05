package com.eduPlazas.eduPlazas.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SecurityConfigTest {

    @Test
    void testPasswordEncoder() {
        SecurityConfig config = new SecurityConfig();
        PasswordEncoder encoder = config.passwordEncoder();
        
        assertThat(encoder).isNotNull();
        // Comprobamos que el encriptador realmente cambia el texto
        assertThat(encoder.encode("miContraseña")).isNotEqualTo("miContraseña"); 
    }
}