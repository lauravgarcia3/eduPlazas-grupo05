package com.eduPlazas.eduPlazas.controller;

import com.eduPlazas.eduPlazas.model.Centro;
import com.eduPlazas.eduPlazas.model.Solicitud;
import com.eduPlazas.eduPlazas.model.Usuario;
import com.eduPlazas.eduPlazas.repository.CentroRepository;
import com.eduPlazas.eduPlazas.repository.SolicitudRepository;
import com.eduPlazas.eduPlazas.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/centro")
public class CentroController {

    private final CentroRepository centroRepository;
    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;

    public CentroController(CentroRepository centroRepository, SolicitudRepository solicitudRepository, UsuarioRepository usuarioRepository) {
        this.centroRepository = centroRepository;
        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        
        // 1. Vemos quién ha iniciado sesión (su correo electrónico)
        String emailUsuario = principal.getName();
        
        // 2. Buscamos el nombre completo del colegio asociado a ese correo
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario).orElse(null);
        
        if(usuario != null) {
            String nombreCentro = usuario.getNombreCompleto();
            
            // 3. Buscamos SOLO las solicitudes para este centro
            List<Solicitud> solicitudes = solicitudRepository.findByCentroPreferencia(nombreCentro);
            
            model.addAttribute("nombreCentro", nombreCentro);
            model.addAttribute("solicitudes", solicitudes);
        }

        return "centro/home"; 
    }
}