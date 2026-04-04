package com.eduPlazas.eduPlazas.controller;

import com.eduPlazas.eduPlazas.model.Usuario;
import com.eduPlazas.eduPlazas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    // Mostrar la pantalla de Login
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Mostrar la pantalla de Registro
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        // Le pasamos un "Usuario" vacío al HTML para que lo rellene
        model.addAttribute("usuario", new Usuario());
        return "register";
    }

    // Recibir los datos cuando el usuario pulsa "Registrarse"
    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("usuario") Usuario usuario) {
        usuarioService.registrarUsuario(usuario);
        // Si todo va bien, lo mandamos al login con un mensajito de éxito
        return "redirect:/login?registrado=true";
    }
}