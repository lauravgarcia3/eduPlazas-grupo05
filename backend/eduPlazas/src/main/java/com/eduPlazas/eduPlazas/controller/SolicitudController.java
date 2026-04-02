package com.eduPlazas.eduPlazas.controller;

import com.eduPlazas.eduPlazas.model.Solicitud;
import com.eduPlazas.eduPlazas.service.SolicitudService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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

@GetMapping("/admin/solicitudes/{id}")
@ResponseBody
public Object verSolicitud(@PathVariable Long id) {
Optional<Solicitud> solicitud = solicitudService.buscarPorId(id);

if (solicitud.isPresent()) {
return solicitud.get();
} else {
return "Solicitud no encontrada";
}
}
@PostMapping("/admin/solicitudes")
@ResponseBody
public Solicitud crearSolicitud(@RequestBody Solicitud solicitud) {
return solicitudService.guardar(solicitud);
}
}