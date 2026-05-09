package com.eduPlazas.eduPlazas.controller;

import static org.mockito.Mockito.times;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.eduPlazas.eduPlazas.model.Centro;
import com.eduPlazas.eduPlazas.model.Convocatoria;
import com.eduPlazas.eduPlazas.model.Solicitud;
import com.eduPlazas.eduPlazas.repository.CentroRepository;
import com.eduPlazas.eduPlazas.repository.SolicitudRepository;
import com.eduPlazas.eduPlazas.service.ConvocatoriaService;
import com.eduPlazas.eduPlazas.service.SolicitudService;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    @Mock
    private CentroRepository centroRepository;
    
    @Mock
    private SolicitudService solicitudService;
    
    @Mock
    private ConvocatoriaService convocatoriaService;
    
    @Mock
    private SolicitudRepository solicitudRepository;
    
    @Mock
    private Model model;

    @InjectMocks
    private AdminController adminController;

    @Test
    void testHome() {
        Convocatoria c1 = new Convocatoria(); 
        c1.setId(1L);
        
        Solicitud s1 = new Solicitud(); 
        s1.setConvocatoria(c1);
        
        when(convocatoriaService.obtenerTodas()).thenReturn(List.of(c1));
        when(solicitudService.obtenerTodas()).thenReturn(List.of(s1));

        String vista = adminController.home(model);

        assertThat(vista).isEqualTo("admin/home");
        verify(model).addAttribute(eq("convocatorias"), any());
        verify(model).addAttribute(eq("solicitudesPorConvocatoria"), any());
    }

    @Test
    void testNuevaConvocatoria() {
        when(centroRepository.findAll()).thenReturn(new ArrayList<>());
        
        String vista = adminController.nuevaConvocatoria(model);
        
        assertThat(vista).isEqualTo("admin/convocatoria");
        verify(model).addAttribute(eq("nuevaConvocatoria"), any(Convocatoria.class));
    }

    @Test
    void testGuardarConvocatoria() {
        // Simulamos una convocatoria que pasa a estado ACTIVA
        Convocatoria conv = new Convocatoria();
        conv.setEstado("ACTIVA");

        Centro centroMock = new Centro();
        centroMock.setNombre("CEIP San Francisco de Asís");
        centroMock.setNumPlazas(0);
        
        when(centroRepository.findAll()).thenReturn(List.of(centroMock));

        // Simulamos que el admin manda los arrays de centros y plazas desde la web
        String nombresCentros = "CEIP San Francisco de Asís";
        String plazasCentros = "40";

        String vista = adminController.guardarConvocatoria(conv, nombresCentros, plazasCentros);

        // Verificamos que redirige, guarda la convocatoria y actualiza el centro con las 40 plazas
        assertThat(vista).isEqualTo("redirect:/admin/home");
        verify(convocatoriaService).guardarConvocatoria(conv);
        verify(centroRepository, times(2)).save(any(Centro.class));
        assertThat(centroMock.getNumPlazas()).isEqualTo(40);
    }

    @Test
    void testEditarConvocatoria() {
        Convocatoria conv = new Convocatoria(); conv.setEstado("ACTIVA");
        when(convocatoriaService.obtenerPorId(1L)).thenReturn(java.util.Optional.of(conv));
        when(centroRepository.findAll()).thenReturn(new java.util.ArrayList<>());
        
        String vista = adminController.editarConvocatoria(1L, model);
        assertThat(vista).isEqualTo("admin/convocatoria");
    }

    @Test
    void testVerSolicitudesPorConvocatoria() {
        Convocatoria conv = new Convocatoria();
        when(convocatoriaService.obtenerPorId(1L)).thenReturn(java.util.Optional.of(conv));
        when(centroRepository.findAll()).thenReturn(new java.util.ArrayList<>());
        
        String vista = adminController.verSolicitudesPorConvocatoria(1L, model);
        assertThat(vista).isEqualTo("admin/publicaciones");
    }

    @Test
    void testAdjudicarSolicitudesConvocatoria() {
        Convocatoria conv = new Convocatoria();
        when(convocatoriaService.obtenerPorId(1L)).thenReturn(java.util.Optional.of(conv));
        when(centroRepository.findAll()).thenReturn(new java.util.ArrayList<>());

        String vista = adminController.adjudicarSolicitudesConvocatoria(1L);
        assertThat(vista).isEqualTo("redirect:/admin/convocatoria/1/solicitudes");
    }
}