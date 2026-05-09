package com.eduPlazas.eduPlazas.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.eduPlazas.eduPlazas.model.Solicitud;
import com.eduPlazas.eduPlazas.model.Usuario;
import com.eduPlazas.eduPlazas.repository.CentroRepository;
import com.eduPlazas.eduPlazas.repository.UsuarioRepository;
import com.eduPlazas.eduPlazas.service.ConvocatoriaService;
import com.eduPlazas.eduPlazas.service.SolicitudService;

@ExtendWith(MockitoExtension.class)
public class SolicitanteControllerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SolicitudService solicitudService;

    @Mock
    private ConvocatoriaService convocatoriaService;

    @Mock
    private CentroRepository centroRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private SolicitanteController solicitanteController;

    @Test
    void testHomeSolicitante() {
        // 1. Preparamos un usuario con dos solicitudes (una completa y otra no)
        when(authentication.getName()).thenReturn("familia@eduplazas.com");
        
        Usuario usuario = new Usuario();
        Solicitud s1 = new Solicitud(); s1.setId(1L); s1.setCompletada(true);
        Solicitud s2 = new Solicitud(); s2.setId(2L); s2.setCompletada(false);
        usuario.setSolicitudes(List.of(s1, s2));
        
        when(usuarioRepository.findByEmail("familia@eduplazas.com")).thenReturn(Optional.of(usuario));
        when(solicitudService.obtenerTotalPuntos(any())).thenReturn(15.0);
        when(convocatoriaService.obtenerConvocatoriaActiva()).thenReturn(Optional.empty());

        // 2. Ejecutamos
        String vista = solicitanteController.home(authentication, model);

        // 3. Comprobamos que separa bien las completas de las incompletas
        assertThat(vista).isEqualTo("solicitante/home");
        verify(model).addAttribute(eq("solicitudesCompletas"), anyList());
        verify(model).addAttribute(eq("solicitudesIncompletas"), anyList());
    }

    @Test
    void testFormularioNuevaSolicitud() {
        // 1. Preparamos el entorno sin ID (nueva solicitud)
        when(convocatoriaService.obtenerConvocatoriaActiva()).thenReturn(Optional.empty());
        when(centroRepository.findAll()).thenReturn(new ArrayList<>());

        // 2. Ejecutamos
        String vista = solicitanteController.formulario(null, model);

        // 3. Comprobamos que nos manda al formulario y crea una solicitud vacía
        assertThat(vista).isEqualTo("solicitante/formulario");
        verify(model).addAttribute(eq("nuevaSolicitud"), any(Solicitud.class));
    }

    @Test
    void testGuardarSolicitudComoBorrador() {
        // 1. Preparamos datos simulando que el usuario le da a "Terminar más tarde"
        when(authentication.getName()).thenReturn("familia@eduplazas.com");
        when(usuarioRepository.findByEmail("familia@eduplazas.com")).thenReturn(Optional.of(new Usuario()));
        when(convocatoriaService.obtenerConvocatoriaActiva()).thenReturn(Optional.empty());

        Solicitud solicitudGuardar = new Solicitud();
        
        // 2. Ejecutamos pasándole la acción "borrador"
        String vista = solicitanteController.guardarSolicitud(
            null, solicitudGuardar, bindingResult, "borrador", null, authentication, model
        );

        // 3. Comprobamos que le pone el estado correcto y llama al servicio de guardado
        assertThat(vista).isEqualTo("redirect:/solicitante/home");
        assertThat(solicitudGuardar.getEstado()).isEqualTo("Borrador");
        assertThat(solicitudGuardar.getCompletada()).isFalse();
        verify(solicitudService).guardar(solicitudGuardar);
    }

    @Test
    void testGuardarSolicitudCompletarConArchivos() {
        // 1. Preparar datos para simular una solicitud definitiva que ya existía (ID = 10)
        when(authentication.getName()).thenReturn("familia@eduplazas.com");
        when(usuarioRepository.findByEmail("familia@eduplazas.com")).thenReturn(Optional.of(new Usuario()));
        when(convocatoriaService.obtenerConvocatoriaActiva()).thenReturn(Optional.empty());
        when(solicitudService.obtenerPorId(10L)).thenReturn(Optional.of(new Solicitud()));

        Solicitud solicitudDefinitiva = new Solicitud();
        solicitudDefinitiva.setId(10L);

        // Simulamos que el formulario NO tiene errores de validación
        when(bindingResult.hasErrors()).thenReturn(false);

        // Creamos un archivo falso simulando un PDF subido por la familia
        org.springframework.mock.web.MockMultipartFile archivoPDF = 
            new org.springframework.mock.web.MockMultipartFile(
                "archivos", "dni_padre.pdf", "application/pdf", "contenido-simulado".getBytes()
            );
        org.springframework.web.multipart.MultipartFile[] documentosSubidos = { archivoPDF };

        // 2. Ejecutar pasándole la acción "completar" y los archivos
        String vista = solicitanteController.guardarSolicitud(
            10L, solicitudDefinitiva, bindingResult, "completar", documentosSubidos, authentication, model
        );

        // 3. Comprobar que procesó el archivo, cambió el estado a Enviada y la guardó
        assertThat(vista).isEqualTo("redirect:/solicitante/home");
        assertThat(solicitudDefinitiva.getEstado()).isEqualTo("Enviada");
        assertThat(solicitudDefinitiva.getCompletada()).isTrue();
        
        // Comprobamos que el sistema extrajo bien el documento
        assertThat(solicitudDefinitiva.getDocumentos()).hasSize(1);
        assertThat(solicitudDefinitiva.getDocumentos().get(0).getNombre()).isEqualTo("dni_padre.pdf");
        
        verify(solicitudService).guardar(solicitudDefinitiva);
    }

    @Test
    void testFormularioRecuperarBorrador() {
        // Simulamos que la familia entra a editar un borrador que ya existe (ID = 99)
        Solicitud borrador = new Solicitud();
        when(solicitudService.obtenerPorId(99L)).thenReturn(java.util.Optional.of(borrador));
        
        // Ejecutamos la carga del formulario
        String vista = solicitanteController.formulario(99L, model);
        
        // Comprobamos que carga bien la vista
        assertThat(vista).isEqualTo("solicitante/formulario");
    }

    @Test
    void testVerEstadoSolicitud() {
        // Simulamos que la familia quiere ver cómo va su solicitud
        Solicitud s = new Solicitud();
        s.setCentroPreferencia("Mi Colegio Favorito");
        when(solicitudService.obtenerPorId(1L)).thenReturn(java.util.Optional.of(s));
        when(centroRepository.findAll()).thenReturn(new java.util.ArrayList<>());
        
        // Ejecutamos la pantalla de estado
        String vista = solicitanteController.estado(1L, model);
        
        assertThat(vista).isEqualTo("solicitante/estado");
    }

    @Test
    void testVerEstadoPantallaVacia() {
        // Simulamos que el usuario entra en la pestaña de estado sin seleccionar ninguna solicitud
        String vista = solicitanteController.estado(null, model);
        assertThat(vista).isEqualTo("solicitante/estado");
    }
}