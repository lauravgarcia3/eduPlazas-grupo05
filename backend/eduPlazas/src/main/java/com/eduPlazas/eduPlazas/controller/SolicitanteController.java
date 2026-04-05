package com.eduPlazas.eduPlazas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import com.eduPlazas.eduPlazas.repository.UsuarioRepository;
import com.eduPlazas.eduPlazas.service.SolicitudService;
import com.eduPlazas.eduPlazas.model.Solicitud;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/solicitante")
public class SolicitanteController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SolicitudService solicitudService;

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        String email = authentication.getName();
        var usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            var usuario = usuarioOpt.get();
            List<Solicitud> solicitudes = usuario.getSolicitudes();

            Map<Long, Double> puntosPorSolicitud = new LinkedHashMap<>();
            for (Solicitud solicitud : solicitudes) {
                puntosPorSolicitud.put(solicitud.getId(), solicitudService.obtenerTotalPuntos(solicitud));
            }

            model.addAttribute("solicitudes", solicitudes);
            model.addAttribute("puntosPorSolicitud", puntosPorSolicitud);
        } else {
            model.addAttribute("solicitudes", new ArrayList<>());
            model.addAttribute("puntosPorSolicitud", new LinkedHashMap<Long, Double>());
        }

        return "solicitante/home";
    }

    @GetMapping("/solicitud")
    public String formulario() {
        return "solicitante/formulario";
    }

    @GetMapping("/estado")
    public String estado() {
        return "solicitante/estado";
    }

    @PostMapping("/solicitud/guardar")
    public String guardarSolicitudIncompleta(@RequestParam Long id) {
        solicitudService.actualizarEstado(id, false);
        return "redirect:/solicitante/home";
    }

    @PostMapping("/solicitud/completar/{id}")
    public String completarSolicitud(@PathVariable Long id) {
        solicitudService.actualizarEstado(id, true);
        return "redirect:/solicitante/home";
    }
}