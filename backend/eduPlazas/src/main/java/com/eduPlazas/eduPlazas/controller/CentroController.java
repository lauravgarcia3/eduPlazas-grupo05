package com.eduPlazas.eduPlazas.controller;

import com.eduPlazas.eduPlazas.model.Centro;
import com.eduPlazas.eduPlazas.model.Solicitud;
import com.eduPlazas.eduPlazas.repository.CentroRepository;
import com.eduPlazas.eduPlazas.repository.SolicitudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/centro")
public class CentroController {

    private final CentroRepository centroRepository;
    private final SolicitudRepository solicitudRepository;

    public CentroController(CentroRepository centroRepository, SolicitudRepository solicitudRepository) {
        this.centroRepository = centroRepository;
        this.solicitudRepository = solicitudRepository;
    }

    @GetMapping("/home")
    public String home(Model model) {
        // Código de tu compañero: Por ahora pasamos un nombre fijo, luego lo sacaremos de la base de datos
        model.addAttribute("nombreCentro", "CEIP San Francisco de Asís");
        return "centro/home"; 
    }

    @GetMapping("/admitidos/{id}")
    public String verAdmitidos(@PathVariable Long id, Model model) {
        // Tu código original intacto
        Centro centro = centroRepository.findById(id).orElse(null);

        if (centro == null) {
            return "redirect:/";
        }

        List<Solicitud> solicitudes = solicitudRepository.findByCentroPreferencia(centro.getNombre());

        model.addAttribute("centro", centro);
        model.addAttribute("solicitudes", solicitudes);

        return "centro/admitidos";
    }
}