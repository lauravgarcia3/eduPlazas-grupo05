package com.eduPlazas.eduPlazas.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eduPlazas.eduPlazas.model.Solicitud;
import com.eduPlazas.eduPlazas.model.Usuario;
import com.eduPlazas.eduPlazas.repository.SolicitudRepository;

@ExtendWith(MockitoExtension.class)
public class SolicitudServiceTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private PuntuacionService puntuacionService; // Simulamos el servicio de puntos

    @InjectMocks
    private SolicitudService solicitudService;

    @Test
    void testObtenerIncompletasPorUsuario() {
        Usuario user = new Usuario();
        Solicitud s1 = new Solicitud(); s1.setCompletada(true);
        Solicitud s2 = new Solicitud(); s2.setCompletada(false); // Incompleta
        
        when(solicitudRepository.findByUsuario(user)).thenReturn(List.of(s1, s2));
        
        List<Solicitud> resultado = solicitudService.obtenerIncompletasPorUsuario(user);
        
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCompletada()).isFalse();
    }

    @Test
    void testGuardarSolicitudCalculaPuntosCorrectamente() {
        // Preparamos una solicitud marcando algunas casillas para que entre por los IFs
        Solicitud sol = new Solicitud();
        sol.setTieneHermanosEnCentro(true); // Debería dar 15 puntos
        sol.setFamiliaNumerosa(true);       // Debería dar 10 puntos

        when(solicitudRepository.save(any(Solicitud.class))).thenReturn(sol);

        // Ejecutamos el guardado
        Solicitud guardada = solicitudService.guardar(sol);

        // Comprobamos que el repositorio la guardó
        assertThat(guardada).isNotNull();
        
        // Verificamos que llamó al PuntuacionService con los puntos exactos (15 y 10)
        verify(puntuacionService).calcularYGuardar(
            any(Solicitud.class),
            org.mockito.ArgumentMatchers.eq(15.0), // hermanos
            org.mockito.ArgumentMatchers.eq(0.0),  // proximidad
            org.mockito.ArgumentMatchers.eq(0.0),  // trabajo
            org.mockito.ArgumentMatchers.eq(10.0), // numerosa
            org.mockito.ArgumentMatchers.eq(0.0),  // monoparental
            org.mockito.ArgumentMatchers.eq(0.0),  // discapacidad
            org.mockito.ArgumentMatchers.eq(0.0),  // renta
            org.mockito.ArgumentMatchers.eq(0.0),  // violencia
            org.mockito.ArgumentMatchers.eq(0.0),  // conciliacion
            org.mockito.ArgumentMatchers.eq(0.0)   // traslado
        );
    }

    @Test
    void testCambiarEstadoExito() {
        Solicitud sol = new Solicitud();
        sol.setId(1L);
        sol.setEstado("ENVIADA"); // Usamos un estado oficial inicial

        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(sol));
        when(solicitudRepository.save(any(Solicitud.class))).thenAnswer(i -> i.getArguments()[0]);

        // Cambiamos "Aceptada" por el término oficial del SDD: "ADMITIDA"
        Solicitud actualizada = solicitudService.cambiarEstado(1L, "ADMITIDA");

        assertThat(actualizada.getEstado()).isEqualTo("ADMITIDA");
    }
}