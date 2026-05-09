package com.eduPlazas.eduPlazas.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import jakarta.validation.Validator;

import com.eduPlazas.eduPlazas.model.Solicitud;
import com.eduPlazas.eduPlazas.model.Usuario;
import com.eduPlazas.eduPlazas.repository.CentroRepository;
import com.eduPlazas.eduPlazas.repository.SolicitudRepository;
import com.eduPlazas.eduPlazas.repository.UsuarioRepository;
import com.eduPlazas.eduPlazas.service.ConvocatoriaService;
import com.eduPlazas.eduPlazas.service.SolicitudService;

@ExtendWith(MockitoExtension.class)
public class SolicitanteControllerTest {

    @Mock
    private SolicitudService solicitudService;

    @Mock
    private ConvocatoriaService convocatoriaService;

    @Mock
    private CentroRepository centroRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private Validator validator;

    @Mock
    private Model model;

    @Mock
    private Authentication authentication;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private SolicitanteController solicitanteController;

    @Test
    void testHome() {
        Usuario u = new Usuario();
        u.setSolicitudes(new ArrayList<>());
        when(authentication.getName()).thenReturn("test@test.com");
        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(Optional.of(u));
        when(convocatoriaService.obtenerConvocatoriaActiva()).thenReturn(Optional.empty());

        String vista = solicitanteController.home(authentication, model);
        assertThat(vista).isEqualTo("solicitante/home");
    }

    @Test
    void testFormularioNuevaSolicitud() {
        String vista = solicitanteController.formulario(null, model, authentication);
        assertThat(vista).isEqualTo("solicitante/formulario");
    }

    @Test
    void testFormularioRecuperarBorrador() {
        Usuario u = new Usuario();
        u.setId(1L);
        when(authentication.getName()).thenReturn("test@test.com");
        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(Optional.of(u));

        Solicitud borrador = new Solicitud();
        borrador.setUsuario(u); // Previene el fallo de seguridad IDOR
        when(solicitudService.obtenerPorId(99L)).thenReturn(Optional.of(borrador));
        
        String vista = solicitanteController.formulario(99L, model, authentication);
        assertThat(vista).isEqualTo("solicitante/formulario");
    }

    @Test
    void testGuardarSolicitudCompletarConArchivos() {
        Usuario u = new Usuario();
        u.setId(1L);
        when(authentication.getName()).thenReturn("familia@eduplazas.com");
        when(usuarioRepository.findByEmail("familia@eduplazas.com")).thenReturn(Optional.of(u));
        
        Solicitud solicitudExistente = new Solicitud();
        solicitudExistente.setId(10L);
        solicitudExistente.setUsuario(u); // Asignamos dueño
        when(solicitudService.obtenerPorId(10L)).thenReturn(Optional.of(solicitudExistente));
        when(convocatoriaService.obtenerConvocatoriaActiva()).thenReturn(Optional.empty());

        Solicitud solicitudDefinitiva = new Solicitud();
        solicitudDefinitiva.setId(10L);

        when(bindingResult.hasErrors()).thenReturn(false);

        org.springframework.mock.web.MockMultipartFile archivoPDF = 
            new org.springframework.mock.web.MockMultipartFile(
                "archivos", "dni_padre.pdf", "application/pdf", "contenido-simulado".getBytes()
            );
        org.springframework.web.multipart.MultipartFile[] documentosSubidos = { archivoPDF };

        String vista = solicitanteController.guardarSolicitud(
            10L, solicitudDefinitiva, bindingResult, "completar", documentosSubidos, authentication, model
        );

        assertThat(vista).isEqualTo("redirect:/solicitante/home");
        assertThat(solicitudDefinitiva.getEstado()).isEqualTo("Enviada");
        assertThat(solicitudDefinitiva.getCompletada()).isTrue();
        assertThat(solicitudDefinitiva.getDocumentos()).hasSize(1);
        
        verify(solicitudService).guardar(solicitudDefinitiva);
    }

    @Test
    void testVerEstadoSolicitud() {
        Usuario u = new Usuario();
        u.setId(1L);
        when(authentication.getName()).thenReturn("test@test.com");
        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(Optional.of(u));

        Solicitud s = new Solicitud();
        s.setUsuario(u); 
        s.setCentroPreferencia("Mi Colegio Favorito");
        
        // Ahora usamos el nuevo repositorio que implementó tu compañero
        when(solicitudRepository.findByIdWithUsuario(1L)).thenReturn(Optional.of(s));
        when(centroRepository.findAll()).thenReturn(new ArrayList<>());
        
        String vista = solicitanteController.estado(1L, model, authentication);
        assertThat(vista).isEqualTo("solicitante/estado");
    }

    @Test
    void testVerEstadoPantallaVacia() {
        // En el nuevo código, si no hay ID, hace redirect en lugar de quedarse en la pantalla
        String vista = solicitanteController.estado(null, model, authentication);
        assertThat(vista).isEqualTo("redirect:/solicitante/home");
    }
}