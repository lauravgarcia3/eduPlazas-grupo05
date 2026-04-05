package com.eduPlazas.eduPlazas.controller;

import com.eduPlazas.eduPlazas.model.*;
import com.eduPlazas.eduPlazas.repository.UsuarioRepository;
import com.eduPlazas.eduPlazas.service.ConvocatoriaService;
import com.eduPlazas.eduPlazas.service.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


@Controller
@RequestMapping("/solicitante")
public class SolicitanteController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SolicitudService solicitudService;

    @Autowired
    private ConvocatoriaService convocatoriaService;

    private Usuario obtenerUsuarioSeguro(Authentication authentication) {
        Usuario usuario = null;
        if (authentication != null) {
            usuario = usuarioRepository.findByEmail(authentication.getName()).orElse(null);
        }
        // Si no hay sesión (en desarrollo), usamos el solicitante por defecto de la DB
        if (usuario == null) {
            usuario = usuarioRepository.findByEmail("solicitante@eduplazas.com").orElse(null);
        }
        return usuario;
    }

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        // 1. Obtenemos el usuario de forma segura
        Usuario usuarioActivo = obtenerUsuarioSeguro(authentication);

        if (usuarioActivo != null) {
            // 2. Traemos TODAS las solicitudes de este usuario
            List<Solicitud> todas = solicitudService.obtenerPorUsuario(usuarioActivo);
            
            // 3. Creamos los dos "montones" que el HTML necesita ver
            List<Solicitud> incompletas = new ArrayList<>();
            List<Solicitud> completas = new ArrayList<>();
            
            for (Solicitud s : todas) {
                // Separamos según la columna COMPLETADA de la base de datos
                if (s.getCompletada() != null && s.getCompletada()) {
                    completas.add(s);
                } else {
                    incompletas.add(s);
                }
            }
            
            // 4. Enviamos las listas con los nombres que usa el home.html
            model.addAttribute("solicitudesIncompletas", incompletas);
            model.addAttribute("solicitudesCompletas", completas);
        } else {
            model.addAttribute("solicitudesIncompletas", new ArrayList<>());
            model.addAttribute("solicitudesCompletas", new ArrayList<>());
        }

        // --- Resto del código de Convocatoria (fechas) ---
        Optional<Convocatoria> activa = convocatoriaService.obtenerConvocatoriaActiva();
        if (activa.isPresent()) {
            Convocatoria conv = activa.get();
            model.addAttribute("convocatoriaActiva", conv);

            if (conv.getFechaInicio() != null && conv.getFechaFin() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMMM", new Locale("es", "ES"));
                model.addAttribute("fechaInicioFormat", conv.getFechaInicio().format(formatter));
                model.addAttribute("fechaFinFormat", conv.getFechaFin().format(formatter));
                model.addAttribute("fechaProvisionales", conv.getFechaFin().plusDays(10).format(formatter));
                model.addAttribute("fechaReclamaciones", conv.getFechaFin().plusDays(10).format(formatter) + " - " + conv.getFechaFin().plusDays(20).format(formatter));
                model.addAttribute("fechaDefinitivos", conv.getFechaFin().plusDays(30).format(formatter));
            }
        }

        return "solicitante/home";
    }
    @GetMapping("/solicitud")
    public String formulario(Model model) {

        Solicitud nuevaSolicitud = new Solicitud();
        nuevaSolicitud.setMenor(new Menor());
        nuevaSolicitud.setTutor1(new Tutor());
        nuevaSolicitud.setTutor2(new Tutor());
        nuevaSolicitud.setDomicilioFamiliar(new DomicilioFamiliar());
        model.addAttribute("nuevaSolicitud", nuevaSolicitud);

        Optional<Convocatoria> activa = convocatoriaService.obtenerConvocatoriaActiva();
        if (activa.isPresent()) {
            Convocatoria conv = activa.get();
            model.addAttribute("convocatoriaActiva", conv);

            if (conv.getFechaInicio() != null && conv.getFechaFin() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
                model.addAttribute("fechaInicioFormat", conv.getFechaInicio().format(formatter));
                model.addAttribute("fechaFinFormat", conv.getFechaFin().format(formatter));
            }
        }

        return "solicitante/formulario";
    }

@GetMapping("/estado")
    public String estado() {
        return "solicitante/estado";
    }

    @PostMapping("/solicitud/guardar")
    public String guardarSolicitud(@ModelAttribute("nuevaSolicitud") Solicitud solicitud, 
                                   @RequestParam("accion") String accion,
                                   Authentication authentication) {
                                       
        String email = authentication.getName();
        var usuarioOpt = usuarioRepository.findByEmail(email);
        usuarioOpt.ifPresent(solicitud::setUsuario);

        if ("borrador".equals(accion)) {
            solicitud.setCompletada(false);
            solicitud.setEstado("Borrador");
        } else if ("completar".equals(accion)) {
            solicitud.setCompletada(true);
            solicitud.setEstado("Enviada");
        }

        solicitudService.guardar(solicitud);
        return "redirect:/solicitante/home";
    
    }
}