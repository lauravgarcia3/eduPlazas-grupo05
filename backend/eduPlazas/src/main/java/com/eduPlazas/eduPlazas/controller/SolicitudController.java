package com.eduPlazas.eduPlazas.controller;

import com.eduPlazas.eduPlazas.model.Solicitud;
import com.eduPlazas.eduPlazas.service.SolicitudService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class SolicitudController {

private final SolicitudService solicitudService;

public SolicitudController(SolicitudService solicitudService) {
this.solicitudService = solicitudService;
}

// =========================
// ADMIN
// =========================

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
public Solicitud crearSolicitudAdmin(@RequestBody Solicitud solicitud) {
return solicitudService.guardar(solicitud);
}

// =========================
// SOLICITANTE
// =========================

@GetMapping("/solicitante/mis-solicitudes")
@ResponseBody
public List<Solicitud> misSolicitudes() {
String usuario = obtenerUsuarioActual();
return solicitudService.obtenerPorUsuario(usuario);
}

@GetMapping("/solicitante/estado/{id}")
@ResponseBody
public Object verEstadoSolicitud(@PathVariable Long id) {
String usuario = obtenerUsuarioActual();
Optional<Solicitud> solicitud = solicitudService.buscarPorIdYUsuario(id, usuario);

if (solicitud.isPresent()) {
Solicitud s = solicitud.get();

Map<String, Object> response = new LinkedHashMap<>();
response.put("id", s.getId());
response.put("estado", s.getEstado());

if (s.getMenor() != null) {
response.put("nombreMenor", s.getMenor().getNombre() + " " + s.getMenor().getApellidos());
response.put("fechaNacimiento", s.getMenor().getFechaNacimiento());
} else {
response.put("nombreMenor", "");
response.put("fechaNacimiento", "");
}

return response;
} else {
return "Solicitud no encontrada o no pertenece a esta familia";
}
}

@PostMapping("/solicitante/solicitud")
@ResponseBody
public Solicitud crearSolicitudSolicitante(@RequestBody Solicitud solicitud) {

// TODO LOGIN:
// Sustituir este valor simulado por el usuario autenticado real
// cuando sepamos cómo se ha implementado el login.
solicitud.setUsuario(obtenerUsuarioActual());

// Si no viene estado, por defecto una nueva solicitud queda "Pendiente"
if (solicitud.getEstado() == null || solicitud.getEstado().isBlank()) {
solicitud.setEstado("Pendiente");
}

return solicitudService.guardar(solicitud);
}

// =========================
// MÉTODO TEMPORAL PARA LOGIN
// =========================

private String obtenerUsuarioActual() {
// TODO LOGIN:
// Reemplazar este método por la forma real de obtener el usuario autenticado
// (sesión, Spring Security, principal, etc.) cuando vuestro compañero termine el login.
return "familia1";
}
}