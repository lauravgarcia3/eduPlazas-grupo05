package com.eduPlazas.eduPlazas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String estado() {
        return "solicitante/estado";
    }
}