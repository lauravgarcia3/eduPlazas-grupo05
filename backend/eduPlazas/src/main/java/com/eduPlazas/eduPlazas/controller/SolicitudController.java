package com.eduPlazas.eduPlazas.controller;

import com.eduPlazas.eduPlazas.model.Solicitud;
import com.eduPlazas.eduPlazas.service.SolicitudService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SolicitudController {

private final SolicitudService solicitudService;

public SolicitudController(SolicitudService solicitudService) {
this.solicitudService = solicitudService;
}

@GetMapping("/admin/solicitudes")
public String listarSolicitudes(Model model) {
model.addAttribute("solicitudes", solicitudService.obtenerTodas());
return "admin/solicitudes";
}
}
