package com.eduPlazas.eduPlazas.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.eduPlazas.eduPlazas.model.Menor;
import com.eduPlazas.eduPlazas.model.Solicitud;
import com.eduPlazas.eduPlazas.model.Usuario;
import com.eduPlazas.eduPlazas.service.SolicitudService;

@ExtendWith(MockitoExtension.class)
public class SolicitudControllerTest {

    @Mock
    private SolicitudService solicitudService;

    @Mock
    private Model model;

    @InjectMocks
    private SolicitudController solicitudController;

    @Test
    void testListarSolicitudes() {
        when(solicitudService.obtenerTodas()).thenReturn(List.of(new Solicitud()));
        String vista = solicitudController.listarSolicitudes(model);
        assertThat(vista).isEqualTo("admin/solicitudes");
        verify(model).addAttribute(eq("solicitudes"), any());
    }

    @Test
    void testVerSolicitudExiste() {
        Solicitud s = new Solicitud();
        when(solicitudService.buscarPorId(1L)).thenReturn(Optional.of(s));
        Object resultado = solicitudController.verSolicitud(1L);
        assertThat(resultado).isEqualTo(s);
    }

    @Test
    void testCambiarEstado() {
        Solicitud s = new Solicitud();
        s.setEstado("ADMITIDA");
        when(solicitudService.cambiarEstado(1L, "ADMITIDA")).thenReturn(s);
        
        Solicitud resultado = solicitudController.cambiarEstado(1L, "ADMITIDA");
        assertThat(resultado.getEstado()).isEqualTo("ADMITIDA");
    }

    @Test
    void testMisSolicitudes() {
        when(solicitudService.obtenerPorUsuario(any(Usuario.class))).thenReturn(List.of(new Solicitud()));
        List<Solicitud> resultado = solicitudController.misSolicitudes();
        assertThat(resultado).hasSize(1);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testVerEstadoSolicitudConMenor() {
        Solicitud s = new Solicitud();
        s.setId(10L);
        s.setEstado("Enviada");
        
        Menor menor = new Menor();
        menor.setNombre("Ana");
        menor.setApellidos("García");
        s.setMenor(menor);

        when(solicitudService.buscarPorIdYUsuario(eq(10L), any(Usuario.class))).thenReturn(Optional.of(s));
        
        Object resultado = solicitudController.verEstadoSolicitud(10L);
        
        // Comprobamos que el mapa de respuesta se montó correctamente con los datos del menor
        assertThat(resultado).isInstanceOf(Map.class);
        Map<String, Object> map = (Map<String, Object>) resultado;
        assertThat(map.get("estado")).isEqualTo("Enviada");
        assertThat(map.get("nombreMenor")).isEqualTo("Ana García");
    }

    @Test
    void testCrearSolicitudSolicitante() {
        Solicitud s = new Solicitud();
        when(solicitudService.guardar(any(Solicitud.class))).thenAnswer(i -> i.getArguments()[0]);
        
        Solicitud resultado = solicitudController.crearSolicitudSolicitante(s);
        
        // Comprobamos que por defecto le puso estado "Pendiente" y asignó el usuario simulado
        assertThat(resultado.getEstado()).isEqualTo("Pendiente");
        assertThat(resultado.getUsuario().getEmail()).isEqualTo("solicitante@eduplazas.com");
    }
}