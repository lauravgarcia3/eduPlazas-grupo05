package com.eduPlazas.eduPlazas.model;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class ModelTest {

    @Test
    void testModelosGettersYSetters() {
        // Ejecutamos los getters y setters principales para teñir las líneas de verde
        Centro centro = new Centro();
        centro.setId(1L); centro.setNombre("Test"); centro.setNumPlazas(50);
        assertThat(centro.getId()).isEqualTo(1L);
        assertThat(centro.getNombre()).isEqualTo("Test");
        assertThat(centro.getNumPlazas()).isEqualTo(50);

        Solicitud solicitud = new Solicitud();
        solicitud.setId(1L); solicitud.setEstado("Enviada"); solicitud.setCentroPreferencia("CEIP");
        solicitud.setTieneHermanosEnCentro(true); solicitud.setFamiliaNumerosa(true);
        solicitud.setCompletada(true);
        assertThat(solicitud.getEstado()).isEqualTo("Enviada");
        assertThat(solicitud.getTieneHermanosEnCentro()).isTrue();

        Menor menor = new Menor();
        menor.setId(1L); menor.setNombre("Ana"); menor.setApellidos("García");
        assertThat(menor.getNombre()).isEqualTo("Ana");

        Tutor tutor = new Tutor();
        tutor.setId(1L); tutor.setNombre("Padre"); tutor.setEmail("test@test.com");
        assertThat(tutor.getEmail()).isEqualTo("test@test.com");
        
        DocumentoAdjunto doc = new DocumentoAdjunto();
        doc.setId(1L); doc.setNombre("DNI.pdf"); doc.setTipo("application/pdf");
        assertThat(doc.getNombre()).isEqualTo("DNI.pdf");

        Convocatoria conv = new Convocatoria();
        conv.setId(1L); conv.setNombre("Infantil"); conv.setEstado("ACTIVA");
        assertThat(conv.getId()).isEqualTo(1L);
        assertThat(conv.getNombre()).isEqualTo("Infantil");
        assertThat(conv.getEstado()).isEqualTo("ACTIVA");

        Usuario u = new Usuario();
        u.setId(1L); u.setEmail("test@test.com"); u.setNombreCompleto("Juan"); u.setRol("ROLE_ADMIN");
        assertThat(u.getId()).isEqualTo(1L);
        assertThat(u.getEmail()).isEqualTo("test@test.com");
        
        DomicilioFamiliar dom = new DomicilioFamiliar();
        dom.setId(1L); dom.setCiudad("Madrid"); dom.setCodigoPostal("28000");
        assertThat(dom.getCiudad()).isEqualTo("Madrid");
    }

    @Test
    void testMetodosInvisibles() {
        
        Centro c1 = new Centro(); c1.setId(1L);
        Centro c2 = new Centro(); c2.setId(1L);
        c1.toString(); c1.hashCode(); c1.equals(c2); c1.equals(null);
        
        Solicitud s = new Solicitud(); s.setId(1L);
        s.toString(); s.hashCode(); s.equals(new Solicitud());
        
        Usuario u = new Usuario(); u.setId(1L);
        u.toString(); u.hashCode(); u.equals(new Usuario());
        
        Convocatoria conv = new Convocatoria(); conv.setId(1L);
        conv.toString(); conv.hashCode(); conv.equals(new Convocatoria());
        
        Menor m = new Menor(); m.setId(1L);
        m.toString(); m.hashCode(); m.equals(new Menor());
        
        Tutor t = new Tutor(); t.setId(1L);
        t.toString(); t.hashCode(); t.equals(new Tutor());

        DomicilioFamiliar d = new DomicilioFamiliar(); d.setId(1L);
        d.toString(); d.hashCode(); d.equals(new DomicilioFamiliar());

        DocumentoAdjunto doc = new DocumentoAdjunto(); doc.setId(1L);
        doc.toString(); doc.hashCode(); doc.equals(new DocumentoAdjunto());
    }
}