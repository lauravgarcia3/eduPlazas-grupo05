package com.eduPlazas.eduPlazas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/solicitante/home")
    public String solicitanteHome() {
        return "solicitante-home";
    }

    @GetMapping("/admin/home")
    public String adminHome() {
        return "admin-home";
    }
}