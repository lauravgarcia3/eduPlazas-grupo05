package com.eduPlazas.eduPlazas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.security.Principal;

@Controller
@RequestMapping("/centro")
public class CentroController {

    @GetMapping("/home")
    public String home(Model model) {
        // Por ahora pasamos un nombre fijo, luego lo sacaremos de la base de datos
        model.addAttribute("nombreCentro", "CEIP San Francisco de Asís");
        return "centro/home"; 
    }
}