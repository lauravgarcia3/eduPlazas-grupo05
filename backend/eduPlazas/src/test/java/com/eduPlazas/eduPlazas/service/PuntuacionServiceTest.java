package com.eduPlazas.eduPlazas.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eduPlazas.eduPlazas.model.Puntuacion;
import com.eduPlazas.eduPlazas.model.Solicitud;
import com.eduPlazas.eduPlazas.repository.PuntuacionRepository;

@ExtendWith(MockitoExtension.class)
public class PuntuacionServiceTest {

    @Mock
    private PuntuacionRepository puntuacionRepository;

    @InjectMocks
    private PuntuacionService puntuacionService;

    @Test
    void calcularYGuardarTest() {
        // 1. Preparar datos (Arrange)
        Solicitud solicitud = new Solicitud();
        solicitud.setId(1L);

        // Simulamos que al guardar en el repositorio, devuelve el mismo objeto
        when(puntuacionRepository.findBySolicitud(solicitud)).thenReturn(Optional.empty());
        when(puntuacionRepository.save(any(Puntuacion.class))).thenAnswer(i -> i.getArguments()[0]);

        // 2. Ejecutar (Act)
        // Pasamos puntos: 15 (Hermanos) + 12 (Proximidad) + 10 (Numerosa) = 37 puntos en total
        Puntuacion resultado = puntuacionService.calcularYGuardar(
            solicitud, 15.0, 12.0, 0.0, 10.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0
        );

        // 3. Comprobar (Assert) usando AssertJ
        assertThat(resultado).isNotNull();
        assertThat(resultado.getTotalPuntos()).isEqualTo(37.0); // La suma debe ser exacta
        assertThat(resultado.getPuntosHermanos()).isEqualTo(15.0);
        assertThat(resultado.getFechaCalculo()).isNotNull();
    }
}