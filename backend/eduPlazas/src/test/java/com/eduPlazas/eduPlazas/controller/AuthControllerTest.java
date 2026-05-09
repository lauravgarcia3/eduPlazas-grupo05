package com.eduPlazas.eduPlazas.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.eduPlazas.eduPlazas.model.Usuario;
import com.eduPlazas.eduPlazas.service.UsuarioService;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private Model model;

    @InjectMocks
    private AuthController authController;

    @Test
    void testLogin() {
        String vista = authController.login();
        assertThat(vista).isEqualTo("login");
    }

    @Test
    void testShowRegisterForm() {
        String vista = authController.showRegisterForm(model);
        assertThat(vista).isEqualTo("register");
        verify(model).addAttribute(eq("usuario"), any(Usuario.class));
    }

    @Test
    void testProcessRegistration() {
        Usuario nuevoUsuario = new Usuario();
        String vista = authController.processRegistration(nuevoUsuario);
        
        verify(usuarioService).registrarUsuario(nuevoUsuario);
        assertThat(vista).isEqualTo("redirect:/login?registrado=true");
    }
}