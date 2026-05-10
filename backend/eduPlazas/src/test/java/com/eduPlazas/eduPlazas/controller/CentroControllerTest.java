package com.eduPlazas.eduPlazas.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.eduPlazas.eduPlazas.model.Solicitud;
import com.eduPlazas.eduPlazas.model.Usuario;
import com.eduPlazas.eduPlazas.repository.CentroRepository;
import com.eduPlazas.eduPlazas.repository.SolicitudRepository;
import com.eduPlazas.eduPlazas.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class CentroControllerTest {

    @Mock
    private CentroRepository centroRepository;

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private Model model;

    @Mock
    private Principal principal;

    @InjectMocks
    private CentroController centroController;

    @Test
    void testHomeCentro() {
        // 1. Preparar datos falsos (Simulamos la sesión del usuario)
        when(principal.getName()).thenReturn("centro@eduplazas.com");

        Usuario usuarioMock = new Usuario();
        usuarioMock.setEmail("centro@eduplazas.com");
        usuarioMock.setNombreCompleto("CEIP Los Almendros");

        Solicitud solMock = new Solicitud();
        solMock.setCentroPreferencia1("CEIP Los Almendros");

        // Simulamos la base de datos
        when(usuarioRepository.findByEmail("centro@eduplazas.com")).thenReturn(Optional.of(usuarioMock));
        when(solicitudRepository.findByCentroAdjudicado("CEIP Los Almendros")).thenReturn(List.of());
        when(solicitudRepository.findByCentroPreferencia1("CEIP Los Almendros")).thenReturn(List.of(solMock));

        // 2. Ejecutar el método del controlador DIRECTAMENTE
        String vista = centroController.home(model, principal);

        // 3. Comprobar los resultados (Assert)
        assertThat(vista).isEqualTo("centro/home"); // Verifica que nos lleva al HTML correcto
        verify(model).addAttribute("nombreCentro", "CEIP Los Almendros"); // Verifica que pintó el nombre
        verify(model).addAttribute("solicitudes", List.of(solMock)); // Verifica que pintó las solicitudes
    }
}
