package com.eduPlazas.eduPlazas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.eduPlazas.eduPlazas.repository.UsuarioRepository;
import com.eduPlazas.eduPlazas.repository.CentroRepository;
import com.eduPlazas.eduPlazas.service.SolicitudService;
import com.eduPlazas.eduPlazas.service.ConvocatoriaService;
import com.eduPlazas.eduPlazas.model.Solicitud;
import com.eduPlazas.eduPlazas.model.DomicilioFamiliar;
import com.eduPlazas.eduPlazas.model.Menor;
import com.eduPlazas.eduPlazas.model.Tutor;
import com.eduPlazas.eduPlazas.model.Centro;
import com.eduPlazas.eduPlazas.model.Convocatoria;
import com.eduPlazas.eduPlazas.model.DocumentoAdjunto;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/solicitante")
public class SolicitanteController {

    @Autowired
    private CentroRepository centroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SolicitudService solicitudService;

    @Autowired
    private ConvocatoriaService convocatoriaService;

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        String email = authentication.getName();
        var usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            var usuario = usuarioOpt.get();
            List<Solicitud> todas = usuario.getSolicitudes();

            Map<Long, Double> puntosPorSolicitud = new LinkedHashMap<>();
            for (Solicitud solicitud : todas) {
                puntosPorSolicitud.put(solicitud.getId(), solicitudService.obtenerTotalPuntos(solicitud));
            }
            model.addAttribute("puntosPorSolicitud", puntosPorSolicitud);

            List<Solicitud> incompletas = new ArrayList<>();
            List<Solicitud> completas = new ArrayList<>();
            
            for (Solicitud s : todas) {
                if (s.getCompletada() != null && s.getCompletada()) {
                    completas.add(s);
                } else {
                    incompletas.add(s);
                }
            }
            
            model.addAttribute("solicitudesIncompletas", incompletas);
            model.addAttribute("solicitudesCompletas", completas);
            
        } else {
            model.addAttribute("solicitudes", new ArrayList<>());
            model.addAttribute("solicitudesIncompletas", new ArrayList<>());
            model.addAttribute("solicitudesCompletas", new ArrayList<>());
            model.addAttribute("puntosPorSolicitud", new LinkedHashMap<Long, Double>());
        }

        Optional<Convocatoria> activa = convocatoriaService.obtenerConvocatoriaActiva();
        if (activa.isPresent()) {
            Convocatoria conv = activa.get();
            model.addAttribute("convocatoriaActiva", conv);

            if (conv.getFechaInicio() != null && conv.getFechaFin() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMMM", new Locale("es", "ES"));
                model.addAttribute("fechaInicioFormat", conv.getFechaInicio().format(formatter));
                model.addAttribute("fechaFinFormat", conv.getFechaFin().format(formatter));
                model.addAttribute("fechaProvisionales", conv.getFechaFin().plusDays(10).format(formatter));
                model.addAttribute("fechaReclamaciones", conv.getFechaFin().plusDays(10).format(formatter) + " - " + conv.getFechaFin().plusDays(20).format(formatter));
                model.addAttribute("fechaDefinitivos", conv.getFechaFin().plusDays(30).format(formatter));
            }
        }

        return "solicitante/home";
    }

    @GetMapping("/solicitud")
    public String formulario(@RequestParam(value = "id", required = false) Long id, Model model) {
        Solicitud solicitudAMostrar;

        if (id != null) {
            Optional<Solicitud> existente = solicitudService.obtenerPorId(id);
            if (existente.isPresent()) {
                solicitudAMostrar = existente.get();
                if (solicitudAMostrar.getMenor() == null) solicitudAMostrar.setMenor(new Menor());
                if (solicitudAMostrar.getTutor1() == null) solicitudAMostrar.setTutor1(new Tutor());
                if (solicitudAMostrar.getTutor2() == null) solicitudAMostrar.setTutor2(new Tutor());
                if (solicitudAMostrar.getDomicilioFamiliar() == null) solicitudAMostrar.setDomicilioFamiliar(new DomicilioFamiliar());
            } else {
                return "redirect:/solicitante/home";
            }
        } else {
            solicitudAMostrar = new Solicitud();
            solicitudAMostrar.setMenor(new Menor());
            solicitudAMostrar.setTutor1(new Tutor());
            solicitudAMostrar.setTutor2(new Tutor());
            solicitudAMostrar.setDomicilioFamiliar(new DomicilioFamiliar());
        }

        model.addAttribute("nuevaSolicitud", solicitudAMostrar);

        Optional<Convocatoria> activa = convocatoriaService.obtenerConvocatoriaActiva();
        if (activa.isPresent()) {
            Convocatoria conv = activa.get();
            model.addAttribute("convocatoriaActiva", conv);

            if (conv.getFechaInicio() != null && conv.getFechaFin() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
                model.addAttribute("fechaInicioFormat", conv.getFechaInicio().format(formatter));
                model.addAttribute("fechaFinFormat", conv.getFechaFin().format(formatter));
            }
        }

        return "solicitante/formulario";
    }

   @GetMapping("/estado")
    public String estado(@RequestParam(value = "id", required = false) Long id, Model model) {
        
        // 1. Si entran sin pasar un ID (por error o modificando la URL), los mandamos al home
        if (id == null) {
            return "redirect:/solicitante/home";
        }

        Optional<Solicitud> solicitudOpt = solicitudService.obtenerPorId(id);
        
        if (solicitudOpt.isPresent()) {
            Solicitud solicitud = solicitudOpt.get();
            model.addAttribute("solicitud", solicitud);
            
            // Buscamos el centro basándonos en el nombre que se eligió en la solicitud
            if (solicitud.getCentroPreferencia() != null) {
                Optional<Centro> centroOpt = centroRepository.findAll().stream()
                    .filter(c -> c.getNombre().equals(solicitud.getCentroPreferencia()))
                    .findFirst();
                centroOpt.ifPresent(centro -> model.addAttribute("centro", centro));
            }
        } else {
            return "redirect:/solicitante/home"; // Si el ID no existe en Base de Datos, al home
        }
        
        return "solicitante/estado";
    }

@PostMapping("/solicitud/guardar")
    public String guardarSolicitud(
            @RequestParam(value = "id", required = false) Long id, // Capturamos el ID explícitamente
            @Valid @ModelAttribute("nuevaSolicitud") Solicitud solicitud,
            BindingResult result,
            @RequestParam(value = "accion", required = false, defaultValue = "completar") String accion,
            @RequestParam(value = "archivos", required = false) MultipartFile[] documentos,
            Authentication authentication,
            Model model) {

        // 1. FORZAMOS EL ID PARA QUE HIBERNATE ACTUALICE EN LUGAR DE DUPLICAR
        if (id != null) {
            solicitud.setId(id);
        }

        // 2. SI ES COMPLETAR Y HAY ERRORES, VOLVEMOS AL FORMULARIO.
        // SI ES BORRADOR, IGNORAMOS LOS ERRORES (es normal que un borrador esté a medias)
        if ("completar".equals(accion) && result.hasErrors()) {
            Optional<Convocatoria> activa = convocatoriaService.obtenerConvocatoriaActiva();
            if (activa.isPresent()) {
                Convocatoria conv = activa.get();
                model.addAttribute("convocatoriaActiva", conv);
                if (conv.getFechaInicio() != null && conv.getFechaFin() != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
                    model.addAttribute("fechaInicioFormat", conv.getFechaInicio().format(formatter));
                    model.addAttribute("fechaFinFormat", conv.getFechaFin().format(formatter));
                }
            }
            return "solicitante/formulario";
        }

        // 3. ASIGNAMOS USUARIO Y CONVOCATORIA
        String email = authentication.getName();
        var usuarioOpt = usuarioRepository.findByEmail(email);
        usuarioOpt.ifPresent(solicitud::setUsuario);

        Optional<Convocatoria> activa = convocatoriaService.obtenerConvocatoriaActiva();
        activa.ifPresent(solicitud::setConvocatoria);

        // 4. ESTABLECEMOS EL ESTADO
        if ("borrador".equals(accion)) {
            solicitud.setCompletada(false);
            solicitud.setEstado("Borrador");
        } else if ("completar".equals(accion)) {
            solicitud.setCompletada(true);
            solicitud.setEstado("Enviada");
        }

        // 5. REVISIÓN INTELIGENTE DEL TUTOR 2
        if (solicitud.getTutor2() != null) {
            Tutor tutor2 = solicitud.getTutor2();

            boolean tutor2Vacio =
                    (tutor2.getNombre() == null || tutor2.getNombre().isBlank()) &&
                    (tutor2.getApellidos() == null || tutor2.getApellidos().isBlank()) &&
                    (tutor2.getDniNie() == null || tutor2.getDniNie().isBlank()) &&
                    (tutor2.getRelacionConMenor() == null || tutor2.getRelacionConMenor().isBlank()) &&
                    (tutor2.getTelefono() == null || tutor2.getTelefono().isBlank()) &&
                    (tutor2.getEmail() == null || tutor2.getEmail().isBlank()) &&
                    (tutor2.getSituacionLaboral() == null || tutor2.getSituacionLaboral().isBlank());

            if (tutor2Vacio) {
                solicitud.setTutor2(null);
            } else {
                boolean tutor2Incompleto =
                        tutor2.getNombre() == null || tutor2.getNombre().isBlank() ||
                        tutor2.getApellidos() == null || tutor2.getApellidos().isBlank() ||
                        tutor2.getDniNie() == null || tutor2.getDniNie().isBlank() ||
                        tutor2.getRelacionConMenor() == null || tutor2.getRelacionConMenor().isBlank() ||
                        tutor2.getTelefono() == null || tutor2.getTelefono().isBlank() ||
                        tutor2.getEmail() == null || tutor2.getEmail().isBlank() ||
                        tutor2.getSituacionLaboral() == null || tutor2.getSituacionLaboral().isBlank();

                // ¡Sólo damos error del Tutor 2 si van a enviar la solicitud final!
                if ("completar".equals(accion) && tutor2Incompleto) {
                    result.rejectValue("tutor2.nombre", "error.tutor2",
                            "Si cumplimenta los datos del segundo tutor, deberá completar todos sus campos.");

                    if (activa.isPresent()) {
                        Convocatoria conv = activa.get();
                        model.addAttribute("convocatoriaActiva", conv);
                        if (conv.getFechaInicio() != null && conv.getFechaFin() != null) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                                    "d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
                            model.addAttribute("fechaInicioFormat", conv.getFechaInicio().format(formatter));
                            model.addAttribute("fechaFinFormat", conv.getFechaFin().format(formatter));
                        }
                    }
                    return "solicitante/formulario";
                }
            }
        }

        // 6. GUARDADO DE DOCUMENTOS
        if (documentos != null) {
            for (MultipartFile archivo : documentos) {
                if (archivo != null && !archivo.isEmpty()) {
                    DocumentoAdjunto doc = new DocumentoAdjunto();
                    doc.setNombre(archivo.getOriginalFilename());
                    doc.setTipo(archivo.getContentType());
                    try {
                        doc.setContenido(archivo.getBytes());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    doc.setSolicitud(solicitud);
                    solicitud.getDocumentos().add(doc);
                }
            }
        }

        solicitudService.guardar(solicitud);
        return "redirect:/solicitante/home";
    }
}