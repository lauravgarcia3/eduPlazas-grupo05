package com.eduPlazas.eduPlazas.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.eduPlazas.eduPlazas.model.Usuario;
import com.eduPlazas.eduPlazas.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void testLoadUserByUsername_Encontrado() {
        // Simulamos un usuario en la base de datos
        Usuario usuario = new Usuario();
        usuario.setEmail("test@eduplazas.com");
        usuario.setPassword("secreto123");
        usuario.setRol("ROLE_ADMIN");

        when(usuarioRepository.findByEmail("test@eduplazas.com")).thenReturn(Optional.of(usuario));

        // Ejecutamos el servicio de Spring Security
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("test@eduplazas.com");

        // Comprobamos que convierte bien nuestro Usuario al formato UserDetails de Spring
        assertThat(userDetails.getUsername()).isEqualTo("test@eduplazas.com");
        assertThat(userDetails.getPassword()).isEqualTo("secreto123");
        assertThat(userDetails.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    void testLoadUserByUsername_NoEncontrado() {
        // Simulamos que el correo no existe
        when(usuarioRepository.findByEmail("error@eduplazas.com")).thenReturn(Optional.empty());

        // Verificamos que lanza la excepción correcta
        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("error@eduplazas.com");
        });
    }
}