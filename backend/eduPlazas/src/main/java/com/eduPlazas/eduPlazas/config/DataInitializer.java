package com.eduPlazas.eduPlazas.config;

import com.eduPlazas.eduPlazas.model.*;
import com.eduPlazas.eduPlazas.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                                      ConvocatoriaRepository convocatoriaRepository, SolicitudRepository solicitudRepository,
                                      CentroRepository centroRepository) {
        return args -> {
            System.out.println("--- INICIANDO CARGA DE DATOS DE DEMOSTRACIÓN ---");
            
            poblarCentros(centroRepository);
            poblarUsuarios(usuarioRepository, passwordEncoder, centroRepository);
            poblarConvocatorias(convocatoriaRepository);
            poblarSolicitudes(solicitudRepository, usuarioRepository, convocatoriaRepository);
            
            System.out.println("--- CARGA DE DATOS FINALIZADA CON ÉXITO ---");
        };
    }

    // ==========================================
    // 1. POBLAR USUARIOS
    // ==========================================
    private void poblarUsuarios(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, CentroRepository centroRepository) {
        if (usuarioRepository.count() == 0) {
            List<Usuario> usuarios = new ArrayList<>();

            Usuario admin = new Usuario();
            admin.setEmail("admin@eduplazas.com");
            admin.setPassword(passwordEncoder.encode("admin123*"));
            admin.setNombreCompleto("Administrador Principal");
            admin.setRol("ROLE_ADMIN");
            usuarios.add(admin);

            Usuario solicitante = new Usuario();
            solicitante.setEmail("solicitante@eduplazas.com");
            solicitante.setPassword(passwordEncoder.encode("solicitante123*"));
            solicitante.setNombreCompleto("Familia Ejemplo");
            solicitante.setRol("ROLE_SOLICITANTE");
            usuarios.add(solicitante);

            // Crear un usuario dinámicamente por cada centro registrado
            List<Centro> centros = centroRepository.findAll();
            for (Centro centro : centros) {
                Usuario centroUser = new Usuario();
                centroUser.setEmail(centro.getEmail());
                centroUser.setPassword(passwordEncoder.encode("centro123*"));
                centroUser.setNombreCompleto(centro.getNombre());
                centroUser.setRol("ROLE_CENTRO");
                usuarios.add(centroUser);
            }

            // Generamos 100 familias "fantasma" para repartir el resto de solicitudes
            for (int i = 2; i <= 100; i++) {
                Usuario dummy = new Usuario();
                dummy.setEmail("familia" + i + "@eduplazas.com");
                dummy.setPassword(passwordEncoder.encode("123456*"));
                dummy.setNombreCompleto("Familia Fantasma " + i);
                dummy.setRol("ROLE_SOLICITANTE");
                usuarios.add(dummy);
            }

            usuarioRepository.saveAll(usuarios);
            System.out.println("Usuarios de prueba, accesos para todos los centros y 100 familias fantasma creados.");
        }
    }

    // ==========================================
    // 2. POBLAR CENTROS (Con 40 plazas iniciales)
    // ==========================================
    private void poblarCentros(CentroRepository centroRepository) {
        if (centroRepository.count() == 0) {
            int plazasIniciales = 40; 

            List<Centro> centros = Arrays.asList(
                new Centro("CEIP San Francisco de Asís", "Calle de la Educación, 123", "28001 Madrid", "info@ceipsanfrancisco.edu.es", "+34 91 234 5678", "www.ceipsanfrancisco.edu.es", "https://images.unsplash.com/photo-1580582932707-520aed937b7b?w=900&q=80", plazasIniciales),
                new Centro("CEIP Los Almendros", "Avenida de los Almendros, 45", "28002 Madrid", "contacto@ceipalmendros.edu.es", "+34 91 345 6789", "www.ceipalmendros.edu.es", "https://images.unsplash.com/photo-1509062522246-3755977927d7?w=900&q=80", plazasIniciales),
                new Centro("CEIP El Prado", "Calle del Prado, 67", "28003 Madrid", "info@ceipelprado.edu.es", "+34 91 456 7890", "www.ceipelprado.edu.es", "https://images.unsplash.com/photo-1523050854058-8df90110c9f1?w=900&q=80", plazasIniciales),
                new Centro("CEIP Las Rosas", "Plaza de las Rosas, 12", "28004 Madrid", "contacto@ceiplasrosas.edu.es", "+34 91 567 8901", "www.ceiplasrosas.edu.es", "https://images.unsplash.com/photo-1503676260728-1c00da094a0b?w=900&q=80", plazasIniciales),
                new Centro("CEIP La Colina", "Camino de la Colina, 89", "28005 Madrid", "info@ceiplacolina.edu.es", "+34 91 678 9012", "www.ceiplacolina.edu.es", "https://images.unsplash.com/photo-1497633762265-9d179a990aa6?w=900&q=80", plazasIniciales),
                new Centro("CEIP Los Pinos", "Calle de los Pinos, 34", "28006 Madrid", "contacto@ceiplospinos.edu.es", "+34 91 789 0123", "www.ceiplospinos.edu.es", "https://images.unsplash.com/photo-1512820790803-83ca734da794?w=900&q=80", plazasIniciales),
                new Centro("CEIP El Olivo", "Avenida del Olivo, 56", "28007 Madrid", "info@ceipelolivo.edu.es", "+34 91 890 1234", "www.ceipelolivo.edu.es", "https://images.unsplash.com/photo-1427504494785-3a9ca7044f45?w=900&q=80", plazasIniciales),
                new Centro("CEIP La Fuente", "Plaza de la Fuente, 78", "28008 Madrid", "contacto@ceiplafuente.edu.es", "+34 91 901 2345", "www.ceiplafuente.edu.es", "https://images.unsplash.com/photo-1541339907198-e08756dedf3f?w=900&q=80", plazasIniciales),
                new Centro("CEIP El Parque", "Calle del Parque, 90", "28009 Madrid", "info@ceipelparque.edu.es", "+34 91 012 3456", "www.ceipelparque.edu.es", "https://images.unsplash.com/photo-1577896851231-70ef18881754?w=900&q=80", plazasIniciales),
                new Centro("CEIP Las Estrellas", "Avenida de las Estrellas, 23", "28010 Madrid", "contacto@ceiplasestrellas.edu.es", "+34 91 123 4567", "www.ceiplasestrellas.edu.es", "https://images.unsplash.com/photo-1522202176988-66273c2fd55f?w=900&q=80", plazasIniciales)
            );
            centroRepository.saveAll(centros);
            System.out.println("10 Centros inicializados con 40 plazas cada uno.");
        }
    }

    // ==========================================
    // 3. POBLAR CONVOCATORIAS
    // ==========================================
    private void poblarConvocatorias(ConvocatoriaRepository convocatoriaRepository) {
        if (convocatoriaRepository.count() == 0) {
            Convocatoria conv2024 = new Convocatoria();
            conv2024.setNombre("Educación Infantil 2024-2025");
            conv2024.setFechaInicio(LocalDate.of(2024, 4, 1));
            conv2024.setFechaFin(LocalDate.of(2024, 6, 28));
            conv2024.setEstado("CERRADA");
            conv2024.setNumeroPlazas(350);
            conv2024.setTipo("Educación Infantil");
            conv2024.setAnioAcademico("2024-2025");
            conv2024.setModalidad("Presencial");
            conv2024.setNombreCentro("Múltiples Centros");

            Convocatoria conv2026 = new Convocatoria();
            conv2026.setNombre("Educación Infantil 2026-2027");
            conv2026.setFechaInicio(LocalDate.of(2026, 3, 1));
            conv2026.setFechaFin(LocalDate.of(2026, 5, 31));
            conv2026.setEstado("ACTIVA");
            conv2026.setNumeroPlazas(400); 
            conv2026.setTipo("Educación Infantil");
            conv2026.setAnioAcademico("2026-2027");
            conv2026.setModalidad("Presencial");
            conv2026.setNombreCentro("Múltiples Centros");
            conv2026.setDescripcion("Convocatoria abierta para el curso escolar 2026-2027.");

            convocatoriaRepository.saveAll(List.of(conv2024, conv2026));
            System.out.println("Convocatorias (Histórica y Activa) creadas.");
        }
    }

    // ==========================================
    // 4. POBLAR SOLICITUDES DISTRIBUIDAS (160 Total)
    // ==========================================
    private void poblarSolicitudes(SolicitudRepository solicitudRepository, UsuarioRepository usuarioRepository, ConvocatoriaRepository convocatoriaRepository) {
        if (solicitudRepository.count() == 0) {
            Usuario usuarioPrincipal = usuarioRepository.findByEmail("solicitante@eduplazas.com").orElse(null);
            Convocatoria convocatoriaActiva = convocatoriaRepository.findByEstado("ACTIVA").orElse(null);

            if (usuarioPrincipal == null || convocatoriaActiva == null) return;

            List<Solicitud> nuevasSolicitudes = new ArrayList<>();
            Random random = new Random();

            // ---------------------------------------------------------
            // A) LAS 2 SOLICITUDES DE LA FAMILIA PRINCIPAL
            // ---------------------------------------------------------

            Menor menorLucia = new Menor(null, "Lucía", "García", "2021-05-10", "Madrid", "Femenino");
            Tutor tutorLucia = new Tutor(null, "Juan", "García", "12345678A", "Padre", "600111222", "solicitante@eduplazas.com", "Trabajando");
            DomicilioFamiliar domLucia = new DomicilioFamiliar(null, "Calle Principal 1", "Madrid", "28001", "Madrid");

            Solicitud sLucia = new Solicitud();
            sLucia.setNombreSolicitante("Juan García");
            sLucia.setEstado("Enviada");
            sLucia.setCompletada(true);
            sLucia.setUsuario(usuarioPrincipal);
            sLucia.setMenor(menorLucia);
            sLucia.setTutor1(tutorLucia);
            sLucia.setDomicilioFamiliar(domLucia);
            sLucia.setCentroPreferencia("CEIP San Francisco de Asís");
            sLucia.setCursoSolicitado("Educación Infantil 3 años");
            sLucia.setDeclaracionVeracidad(true);
            sLucia.setAutorizacionProteccionDatos(true);
            sLucia.setConvocatoria(convocatoriaActiva);
            nuevasSolicitudes.add(sLucia);

            Menor menorPablo = new Menor(null, "Pablo", "García", "2022-02-15", "Madrid", "Masculino");
            
            Solicitud sPablo = new Solicitud();
            sPablo.setNombreSolicitante("Juan García");
            sPablo.setEstado("Borrador");
            sPablo.setCompletada(false);
            sPablo.setUsuario(usuarioPrincipal);
            sPablo.setMenor(menorPablo);
            sPablo.setCentroPreferencia("CEIP Los Almendros");
            sPablo.setConvocatoria(convocatoriaActiva);
            nuevasSolicitudes.add(sPablo);

            // ---------------------------------------------------------
            // B) SOLICITUDES FANTASMA (119 Enviadas + 39 Borradores)
            // ---------------------------------------------------------
            
            String[] centros = {
                "CEIP San Francisco de Asís", "CEIP Los Almendros", "CEIP El Prado", 
                "CEIP Las Rosas", "CEIP La Colina", "CEIP Los Pinos", 
                "CEIP El Olivo", "CEIP La Fuente", "CEIP El Parque", "CEIP Las Estrellas"
            };
            String[] nombresTutores = {"Pedro", "María", "Javier", "Laura", "Carlos", "Marta", "Manuel", "Sara", "Antonio", "Irene", "Francisco", "Ana", "José", "Lucía", "Luis", "Elena", "Alberto", "Rosa", "Jorge", "Raquel"};
            String[] nombresMenores = {"Hugo", "Martina", "Alejandro", "Sofia", "Mateo", "Valeria", "Leo", "Daniela", "Alvaro", "Julia", "Marcos", "Alba", "Miguel", "Carmen", "Mario", "Carla", "David", "Paula", "Diego", "Blanca"};
            String[] apellidos = {"Martín", "Pérez", "Gómez", "Ruiz", "Hernández", "Díaz", "Moreno", "Muñoz", "Álvarez", "Romero", "Alonso", "Gutiérrez", "Navarro", "Torres", "Domínguez", "Vázquez", "Ramos", "Gil", "Serrano", "Molina"};

            List<Usuario> otrasFamilias = usuarioRepository.findAll().stream()
                .filter(u -> u.getEmail().startsWith("familia") && u.getRol().equals("ROLE_SOLICITANTE"))
                .toList();

            // 119 Enviadas esparcidas por los coles
            for (int i = 0; i < 119; i++) {
                Usuario u = otrasFamilias.isEmpty() ? usuarioPrincipal : otrasFamilias.get(random.nextInt(otrasFamilias.size()));
                String centroDestino = centros[i % centros.length]; // Mantenemos el reparto equitativo por centros

                // Ahora sí, 100% random
                String nombreMenor = nombresMenores[random.nextInt(nombresMenores.length)];
                String nombreTutor = nombresTutores[random.nextInt(nombresTutores.length)];
                String apellido = apellidos[random.nextInt(apellidos.length)];
                String diaNacimiento = String.format("%02d", random.nextInt(28) + 1);
                String digitoAleatorio = String.valueOf(random.nextInt(10));

                Menor m = new Menor(null, nombreMenor, apellido, "2021-06-" + diaNacimiento, "Madrid", "Otro");
                Tutor t = new Tutor(null, nombreTutor, apellido, "8765432" + digitoAleatorio + "B", "Padre/Madre", "61111111" + digitoAleatorio, u.getEmail(), "Trabajando");
                DomicilioFamiliar d = new DomicilioFamiliar(null, "Calle Secundaria " + i, "Madrid", "2800" + (random.nextInt(9) + 1), "Madrid");

                Solicitud s = new Solicitud();
                s.setNombreSolicitante(nombreTutor + " " + apellido);
                s.setEstado("Enviada");
                s.setCompletada(true);
                s.setUsuario(u); 
                s.setMenor(m);
                s.setTutor1(t);
                s.setDomicilioFamiliar(d);
                s.setCentroPreferencia(centroDestino);
                s.setCursoSolicitado("Educación Infantil 3 años");
                s.setDeclaracionVeracidad(true);
                s.setAutorizacionProteccionDatos(true);
                s.setConvocatoria(convocatoriaActiva);

                nuevasSolicitudes.add(s);
            }

            // 39 Borradores esparcidos
            for (int i = 0; i < 39; i++) {
                Usuario u = otrasFamilias.isEmpty() ? usuarioPrincipal : otrasFamilias.get(random.nextInt(otrasFamilias.size()));
                String centroDestino = centros[(i + 3) % centros.length]; 

                String nombreMenor = nombresMenores[random.nextInt(nombresMenores.length)];
                String nombreTutor = nombresTutores[random.nextInt(nombresTutores.length)];
                String apellido = apellidos[random.nextInt(apellidos.length)];
                String diaNacimiento = String.format("%02d", random.nextInt(28) + 1);

                Menor mB = new Menor(null, nombreMenor, apellido, "2022-08-" + diaNacimiento, "Madrid", "Masculino");
                
                Solicitud b = new Solicitud();
                b.setNombreSolicitante(nombreTutor + " " + apellido);
                b.setEstado("Borrador");
                b.setCompletada(false);
                b.setUsuario(u);
                b.setMenor(mB);
                b.setCentroPreferencia(centroDestino);
                b.setConvocatoria(convocatoriaActiva);

                nuevasSolicitudes.add(b);
            }

            solicitudRepository.saveAll(nuevasSolicitudes);
            System.out.println("160 Solicitudes generadas aleatoriamente: 120 Enviadas (75%) y 40 Borradores (25%).");
        }
    }
}