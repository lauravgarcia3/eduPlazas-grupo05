package com.eduPlazas.eduPlazas.controller;

import com.eduPlazas.eduPlazas.model.Solicitud;
import com.eduPlazas.eduPlazas.model.Usuario;
import com.eduPlazas.eduPlazas.service.SolicitudService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import com.eduPlazas.eduPlazas.repository.UsuarioRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class SolicitudController {

private final SolicitudService solicitudService;
private final UsuarioRepository usuarioRepository;

public SolicitudController(SolicitudService solicitudService, UsuarioRepository usuarioRepository) {
this.solicitudService = solicitudService;
this.usuarioRepository = usuarioRepository;
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

@PutMapping("/admin/solicitudes/{id}/estado")
@ResponseBody
public Solicitud cambiarEstado(@PathVariable Long id, @RequestParam String estado) {
return solicitudService.cambiarEstado(id, estado);
}

// =========================
// SOLICITANTE
// =========================

@GetMapping("/solicitante/mis-solicitudes")
@ResponseBody
public List<Solicitud> misSolicitudes(Authentication authentication) {
String email = authentication.getName();
Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
if (usuario == null) return List.of();
return solicitudService.obtenerPorUsuario(usuario);
}

@GetMapping("/solicitante/estado/{id}")
@ResponseBody
public Object verEstadoSolicitud(@PathVariable Long id, Authentication authentication) {
String email = authentication.getName();
Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
if (usuario == null) return "Usuario no encontrado";

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
public Object crearSolicitudSolicitante(@RequestBody Solicitud solicitud, Authentication authentication) {

String email = authentication.getName();
Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
if (usuario == null) return "Usuario no encontrado";

solicitud.setUsuario(usuario);

// Si no viene estado, por defecto una nueva solicitud queda "Pendiente"
if (solicitud.getEstado() == null || solicitud.getEstado().isBlank()) {
solicitud.setEstado("Pendiente");
}

return solicitudService.guardar(solicitud);
}

}