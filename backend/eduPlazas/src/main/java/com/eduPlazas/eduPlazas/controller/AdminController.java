package com.eduPlazas.eduPlazas.controller;

import com.eduPlazas.eduPlazas.model.Convocatoria;
import com.eduPlazas.eduPlazas.service.ConvocatoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import com.eduPlazas.eduPlazas.service.ConvocatoriaService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ConvocatoriaService convocatoriaService;

    // Mostrar el listado en el Home
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("convocatorias", convocatoriaService.obtenerTodas());
        return "admin/home";
    }

    // Mostrar el formulario vacío
    @GetMapping("/convocatoria")
    public String convocatoria(Model model) {
        model.addAttribute("nuevaConvocatoria", new Convocatoria());
        return "admin/convocatoria";
    }

    // Recibir los datos del formulario y guardar
    @PostMapping("/convocatoria/guardar")
    public String guardarConvocatoria(@ModelAttribute("nuevaConvocatoria") Convocatoria convocatoria) {
        convocatoriaService.guardarConvocatoria(convocatoria);
        return "redirect:/admin/home"; // Le mandamos al home para que vea su nueva convocatoria
    }

    @GetMapping("/publicaciones")
    public String publicaciones() {
        return "admin/publicaciones";
    }
}
