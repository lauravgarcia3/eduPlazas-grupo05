package com.eduPlazas.eduPlazas.model;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class ModelTest {

    @Test
    void testModelosGettersYSetters() {
        Centro centro = new Centro(); centro.setId(1L); centro.setNombre("Test"); centro.setNumPlazas(50);
        assertThat(centro.getId()).isEqualTo(1L); assertThat(centro.getNombre()).isEqualTo("Test");

        Solicitud solicitud = new Solicitud(); solicitud.setId(1L); solicitud.setEstado("Enviada"); 
        solicitud.setTieneHermanosEnCentro(true); solicitud.setCompletada(true);
        assertThat(solicitud.getEstado()).isEqualTo("Enviada");

        Menor menor = new Menor(); menor.setId(1L); menor.setNombre("Ana");
        assertThat(menor.getNombre()).isEqualTo("Ana");

        Tutor tutor = new Tutor(); tutor.setId(1L); tutor.setEmail("test@test.com");
        assertThat(tutor.getEmail()).isEqualTo("test@test.com");

        DocumentoAdjunto doc = new DocumentoAdjunto(); doc.setId(1L); doc.setNombre("DNI.pdf");
        assertThat(doc.getNombre()).isEqualTo("DNI.pdf");

        Convocatoria conv = new Convocatoria(); conv.setId(1L); conv.setEstado("ACTIVA");
        assertThat(conv.getEstado()).isEqualTo("ACTIVA");

        Usuario u = new Usuario(); u.setId(1L); u.setEmail("test@test.com");
        assertThat(u.getEmail()).isEqualTo("test@test.com");
        
        DomicilioFamiliar dom = new DomicilioFamiliar(); dom.setId(1L); dom.setCiudad("Madrid");
        assertThat(dom.getCiudad()).isEqualTo("Madrid");
    }

    @Test
    void testMetodosInvisibles() {
        Centro c1 = new Centro(); c1.setId(1L); Centro c2 = new Centro(); c2.setId(1L);
        c1.toString(); c1.hashCode(); c1.equals(c2); c1.equals(null);
        
        Solicitud s = new Solicitud(); s.setId(1L); s.toString(); s.hashCode(); s.equals(new Solicitud());
        Usuario u = new Usuario(); u.setId(1L); u.toString(); u.hashCode(); u.equals(new Usuario());
        Convocatoria conv = new Convocatoria(); conv.setId(1L); conv.toString(); conv.hashCode(); conv.equals(new Convocatoria());
        Menor m = new Menor(); m.setId(1L); m.toString(); m.hashCode(); m.equals(new Menor());
        Tutor t = new Tutor(); t.setId(1L); t.toString(); t.hashCode(); t.equals(new Tutor());
        DomicilioFamiliar d = new DomicilioFamiliar(); d.setId(1L); d.toString(); d.hashCode(); d.equals(new DomicilioFamiliar());
        DocumentoAdjunto doc = new DocumentoAdjunto(); doc.setId(1L); doc.toString(); doc.hashCode(); doc.equals(new DocumentoAdjunto());
    }
}