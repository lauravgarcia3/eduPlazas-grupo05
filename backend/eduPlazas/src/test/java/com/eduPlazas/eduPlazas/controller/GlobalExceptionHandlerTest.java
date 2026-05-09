package com.eduPlazas.eduPlazas.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.eduPlazas.eduPlazas.model.Centro;
import com.eduPlazas.eduPlazas.model.Convocatoria;
import com.eduPlazas.eduPlazas.repository.CentroRepository;
import com.eduPlazas.eduPlazas.service.ConvocatoriaService;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @Mock
    private ConvocatoriaService convocatoriaService;

    @Mock
    private CentroRepository centroRepository;

    @Mock
    private Model model;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void testHandleMaxUploadSizeExceeded() {
        // Simulamos la excepción de archivo muy grande
        MaxUploadSizeExceededException excepcion = new MaxUploadSizeExceededException(5000);

        // Simulamos una convocatoria con fechas para probar el DateFormatter
        Convocatoria conv = new Convocatoria();
        conv.setFechaInicio(LocalDate.of(2026, 4, 1));
        conv.setFechaFin(LocalDate.of(2026, 4, 30));
        when(convocatoriaService.obtenerConvocatoriaActiva()).thenReturn(Optional.of(conv));

        // Simulamos un par de centros desordenados
        Centro c2 = new Centro(); c2.setNombre("Colegio Z");
        Centro c1 = new Centro(); c1.setNombre("Colegio A");
        List<Centro> centros = new ArrayList<>(List.of(c2, c1));
        when(centroRepository.findAll()).thenReturn(centros);

        // Ejecutamos el manejador de errores
        String vista = globalExceptionHandler.handleMaxUploadSizeExceeded(excepcion, model);

        // Comprobamos resultados
        assertThat(vista).isEqualTo("solicitante/formulario");
        
        // Verifica que inyecta el mensaje de error de límite
        verify(model).addAttribute(eq("fileUploadError"), anyString());
        
        // Verifica que carga el contexto visual y formatea las fechas en español
        verify(model).addAttribute("convocatoriaActiva", conv);
        verify(model).addAttribute("fechaInicioFormat", "1 de abril de 2026");
        verify(model).addAttribute("fechaFinFormat", "30 de abril de 2026");
        
        // Verifica que inyecta la lista de centros
        verify(model).addAttribute(eq("centros"), anyList());
    }
}