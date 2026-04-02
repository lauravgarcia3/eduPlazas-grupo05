package com.eduPlazas.eduPlazas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/solicitante")
public class SolicitanteController {

    @GetMapping("/home")
    public String home() {
        return "solicitante/home";
    }

    @GetMapping("/solicitud")
    public String formulario() {
        return "solicitante/formulario";
    }

    @GetMapping("/estado")
    public String estado(Model model) {
        model.addAttribute("estado", "En proceso");
        return "solicitante/estado";
    }

  @PostMapping("/solicitud")
public String crearSolicitud(
        @RequestParam String nombre,
        @RequestParam String apellidos,
        @RequestParam String fechaNacimiento,
        @RequestParam String centro1,
        @RequestParam String centro2,
        @RequestParam String centro3,
        Model model) {

    if (nombre.isEmpty() || apellidos.isEmpty() || fechaNacimiento.isEmpty()
            || centro1.isEmpty() || centro2.isEmpty() || centro3.isEmpty()) {
        model.addAttribute("error", "Todos los campos son obligatorios");
        return "solicitante/formulario";
    }

    model.addAttribute("estado", "Solicitud enviada correctamente");
    return "solicitante/estado";
}
}