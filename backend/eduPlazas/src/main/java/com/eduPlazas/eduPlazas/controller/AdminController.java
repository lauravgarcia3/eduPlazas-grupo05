package com.eduPlazas.eduPlazas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import com.eduPlazas.eduPlazas.service.ConvocatoriaService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ConvocatoriaService convocatoriaService;

    public AdminController(ConvocatoriaService convocatoriaService) {
        this.convocatoriaService = convocatoriaService;
    }

    @GetMapping("/home")
    public String home() {
        return "admin/home";
    }

    @GetMapping("/convocatoria")
    public String convocatoria(Model model) {
        model.addAttribute("convocatorias", convocatoriaService.findAll());
        return "admin/convocatoria";
    }

	@GetMapping("/convocatoria/nueva")
	public String mostrarFormularioNuevaConvocatoria(Model model) {
		model.addAttribute("convocatoria", new com.eduPlazas.eduPlazas.model.Convocatoria());
		return "admin/convocatoria-form";
	}

	@PostMapping("/convocatoria")
	public String guardarConvocatoria(com.eduPlazas.eduPlazas.model.Convocatoria convocatoria) {
		convocatoriaService.save(convocatoria);
		return "redirect:/admin/convocatoria";
	}
 	@GetMapping("/publicaciones")
	public String publicaciones() {
		return "admin/publicaciones";
	}
}


