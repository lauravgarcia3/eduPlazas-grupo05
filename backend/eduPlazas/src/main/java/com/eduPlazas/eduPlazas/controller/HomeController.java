package com.eduPlazas.eduPlazas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        // Redirige automáticamente a la pantalla de login al entrar en la raíz
        return "redirect:/login";
    }

}