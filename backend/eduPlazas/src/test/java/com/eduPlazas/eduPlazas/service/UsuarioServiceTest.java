package com.eduPlazas.eduPlazas.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.eduPlazas.eduPlazas.model.Usuario;
import com.eduPlazas.eduPlazas.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void testRegistrarUsuario() {
        // 1. Preparamos un usuario nuevo con una contraseña sin encriptar
        Usuario usuario = new Usuario();
        usuario.setPassword("clave_secreta");

        // Simulamos que el encriptador de Spring devuelve una contraseña cifrada
        when(passwordEncoder.encode("clave_secreta")).thenReturn("clave_encriptada_$$$");

        // 2. Ejecutamos el servicio
        usuarioService.registrarUsuario(usuario);

        // 3. Comprobamos que el servicio cambió la contraseña, asignó el rol correcto y lo guardó
        assertThat(usuario.getPassword()).isEqualTo("clave_encriptada_$$$");
        assertThat(usuario.getRol()).isEqualTo("ROLE_SOLICITANTE");
        verify(usuarioRepository).save(usuario);
    }
}