package com.eduPlazas.eduPlazas;

import com.eduPlazas.eduPlazas.repository.CentroRepository;
import com.eduPlazas.eduPlazas.repository.UsuarioRepository;
import com.eduPlazas.eduPlazas.repository.ConvocatoriaRepository;
import com.eduPlazas.eduPlazas.repository.SolicitudRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class EduPlazasApplicationTests {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CentroRepository centroRepository;

    @Autowired
    private ConvocatoriaRepository convocatoriaRepository;

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Test
    void contextLoadsAndDemoDataIsPresent() {
        // 1. Verificamos que el contexto de Spring carga sin errores
        // 2. Comprobamos que el DataInitializer ha inyectado los datos de prueba
        
        assertTrue(usuarioRepository.count() >= 100, "Error: Faltan los usuarios demo (debería haber al menos 100).");
        assertTrue(centroRepository.count() >= 10, "Error: Faltan los centros educativos demo (debería haber 10).");
        assertTrue(convocatoriaRepository.count() >= 2, "Error: Faltan las convocatorias demo.");
        assertTrue(solicitudRepository.count() >= 160, "Error: Faltan las solicitudes generadas aleatoriamente.");
        
        System.out.println("✅ Pruebas del sistema superadas: El contexto carga y los datos demo están en la base de datos.");
    }
}