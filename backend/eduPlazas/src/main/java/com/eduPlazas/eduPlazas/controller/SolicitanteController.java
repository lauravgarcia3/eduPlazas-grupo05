package com.eduPlazas.eduPlazas.controller;

import com.eduPlazas.eduPlazas.model.DomicilioFamiliar;
import com.eduPlazas.eduPlazas.model.Menor;
import com.eduPlazas.eduPlazas.model.Solicitud;
import com.eduPlazas.eduPlazas.model.Tutor;
import com.eduPlazas.eduPlazas.model.Usuario;
import com.eduPlazas.eduPlazas.repository.UsuarioRepository;
import com.eduPlazas.eduPlazas.service.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.eduPlazas.eduPlazas.model.DocumentoAdjunto;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

import java.util.ArrayList;

@Controller
@RequestMapping("/solicitante")
public class SolicitanteController {

@Autowired
private UsuarioRepository usuarioRepository;

@Autowired
private SolicitudService solicitudService;

@GetMapping("/home")
public String home(Authentication authentication, Model model) {
String email = authentication.getName();
var usuarioOpt = usuarioRepository.findByEmail(email);

if (usuarioOpt.isPresent()) {
var usuario = usuarioOpt.get();
model.addAttribute("solicitudes", usuario.getSolicitudes());
} else {
model.addAttribute("solicitudes", new ArrayList<>());
}

return "solicitante/home";
}

@GetMapping("/solicitud")
public String formulario() {
return "solicitante/formulario";
}

@GetMapping("/estado")
public String estado() {
return "solicitante/estado";
}

@PostMapping("/solicitud/guardar-formulario")
public String guardarFormularioSolicitud(
Authentication authentication,
@RequestParam String nombreMenor,
@RequestParam String apellidosMenor,
@RequestParam String fechaNacimiento,
@RequestParam String lugarNacimiento,
@RequestParam String sexo,

@RequestParam String nombreTutor1,
@RequestParam String apellidosTutor1,
@RequestParam String dniTutor1,
@RequestParam String relacionTutor1,
@RequestParam String telefonoTutor1,
@RequestParam String emailTutor1,
@RequestParam String situacionLaboral1,

@RequestParam(required = false) String nombreTutor2,
@RequestParam(required = false) String apellidosTutor2,
@RequestParam(required = false) String dniTutor2,
@RequestParam(required = false) String relacionTutor2,
@RequestParam(required = false) String telefonoTutor2,
@RequestParam(required = false) String emailTutor2,
@RequestParam(required = false) String situacionLaboral2,

@RequestParam String direccion,
@RequestParam String ciudad,
@RequestParam String codigoPostal,
@RequestParam String provincia,

@RequestParam String centroPreferencia,
@RequestParam String cursoSolicitado,

@RequestParam(defaultValue = "false") boolean declaracionVeracidad,
@RequestParam(defaultValue = "false") boolean autorizacionProteccionDatos,
@RequestParam(required = false) MultipartFile[] documentos

) {
String email = authentication.getName();
var usuarioOpt = usuarioRepository.findByEmail(email);

if (usuarioOpt.isEmpty()) {
return "redirect:/login";
}

Usuario usuario = usuarioOpt.get();

Menor menor = new Menor();
menor.setNombre(nombreMenor);
menor.setApellidos(apellidosMenor);
menor.setFechaNacimiento(fechaNacimiento);
menor.setLugarNacimiento(lugarNacimiento);
menor.setSexo(sexo);

Tutor tutor1 = new Tutor();
tutor1.setNombre(nombreTutor1);
tutor1.setApellidos(apellidosTutor1);
tutor1.setDniNie(dniTutor1);
tutor1.setRelacionConMenor(relacionTutor1);
tutor1.setTelefono(telefonoTutor1);
tutor1.setEmail(emailTutor1);
tutor1.setSituacionLaboral(situacionLaboral1);

Tutor tutor2 = null;
if (nombreTutor2 != null && !nombreTutor2.isBlank()) {
tutor2 = new Tutor();
tutor2.setNombre(nombreTutor2);
tutor2.setApellidos(apellidosTutor2);
tutor2.setDniNie(dniTutor2);
tutor2.setRelacionConMenor(relacionTutor2);
tutor2.setTelefono(telefonoTutor2);
tutor2.setEmail(emailTutor2);
tutor2.setSituacionLaboral(situacionLaboral2);
}

DomicilioFamiliar domicilio = new DomicilioFamiliar();
domicilio.setDireccionCompleta(direccion);
domicilio.setCiudad(ciudad);
domicilio.setCodigoPostal(codigoPostal);
domicilio.setProvincia(provincia);

Solicitud solicitud = new Solicitud();
solicitud.setNombreSolicitante(usuario.getNombreCompleto());
solicitud.setUsuario(usuario);
solicitud.setEstado("Pendiente");
solicitud.setMenor(menor);
solicitud.setTutor1(tutor1);
solicitud.setTutor2(tutor2);
solicitud.setDomicilioFamiliar(domicilio);
solicitud.setCentroPreferencia(centroPreferencia);
solicitud.setCursoSolicitado(cursoSolicitado);
solicitud.setDeclaracionVeracidad(declaracionVeracidad);
solicitud.setAutorizacionProteccionDatos(autorizacionProteccionDatos);

List<DocumentoAdjunto> listaDocumentos = new ArrayList<>();

if (documentos != null) {
for (MultipartFile archivo : documentos) {
if (archivo != null && !archivo.isEmpty()) {
DocumentoAdjunto doc = new DocumentoAdjunto();
doc.setNombreArchivo(archivo.getOriginalFilename());
doc.setTipoDocumento("Adjunto");
doc.setSolicitud(solicitud);
listaDocumentos.add(doc);
}
}
}

solicitud.setDocumentos(listaDocumentos);

solicitudService.guardar(solicitud);

return "redirect:/solicitante/home";
}

@PostMapping("/solicitud/guardar")
public String guardarSolicitudIncompleta(@RequestParam Long id) {
solicitudService.actualizarEstado(id, false);
return "redirect:/solicitante/home";
}

@PostMapping("/solicitud/completar/{id}")
public String completarSolicitud(@PathVariable Long id) {
solicitudService.actualizarEstado(id, true);
return "redirect:/solicitante/home";
}
}