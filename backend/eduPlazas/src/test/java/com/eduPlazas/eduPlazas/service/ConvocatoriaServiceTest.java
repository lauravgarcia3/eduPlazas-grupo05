package com.eduPlazas.eduPlazas.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eduPlazas.eduPlazas.model.Convocatoria;
import com.eduPlazas.eduPlazas.repository.ConvocatoriaRepository;
import com.eduPlazas.eduPlazas.repository.SolicitudRepository;

@ExtendWith(MockitoExtension.class)
public class ConvocatoriaServiceTest {

    @Mock
    private ConvocatoriaRepository repository;

    @Mock
    private SolicitudRepository solicitudRepository;

    @InjectMocks
    private ConvocatoriaService convocatoriaService;

    @Test
    void testGuardarConvocatoriaReemplazaActiva() {
        // Simulamos que vamos a guardar una NUEVA como ACTIVA
        Convocatoria nueva = new Convocatoria();
        nueva.setEstado("ACTIVA");

        // Simulamos que ya había una VIEJA activa en la base de datos
        Convocatoria vieja = new Convocatoria();
        vieja.setEstado("ACTIVA");

        when(repository.findByEstado("ACTIVA")).thenReturn(Optional.of(vieja));

        // Ejecutamos
        convocatoriaService.guardarConvocatoria(nueva);

        // Comprobamos que a la vieja se le cambió el estado a CERRADA y se guardaron ambas
        assertThat(vieja.getEstado()).isEqualTo("CERRADA");
        verify(repository).save(vieja);
        verify(repository).save(nueva);
    }

    @Test
    void testGuardarConvocatoriaNoActiva() {
        // Simulamos guardar un borrador
        Convocatoria nueva = new Convocatoria();
        nueva.setEstado("BORRADOR");

        convocatoriaService.guardarConvocatoria(nueva);

        // Verificamos que no buscó anteriores y simplemente la guardó
        verify(repository, never()).findByEstado(anyString());
        verify(repository).save(nueva);
    }

    @Test
    void testConsultasBasicas() {
        when(repository.findAllOrdered()).thenReturn(List.of(new Convocatoria()));
        assertThat(convocatoriaService.obtenerTodas()).hasSize(1);

        when(repository.findById(1L)).thenReturn(Optional.of(new Convocatoria()));
        assertThat(convocatoriaService.obtenerPorId(1L)).isPresent();

        when(repository.findByEstado("ACTIVA")).thenReturn(Optional.of(new Convocatoria()));
        assertThat(convocatoriaService.obtenerConvocatoriaActiva()).isPresent();

        Convocatoria c = new Convocatoria();
        when(solicitudRepository.countByConvocatoria(c)).thenReturn(10L);
        assertThat(convocatoriaService.contarSolicitudesPorConvocatoria(c)).isEqualTo(10L);
    }
}