package com.eduPlazas.eduPlazas.controller;

import com.eduPlazas.eduPlazas.model.Convocatoria;
import com.eduPlazas.eduPlazas.model.Solicitud;
import com.eduPlazas.eduPlazas.model.Centro;
import com.eduPlazas.eduPlazas.repository.SolicitudRepository;
import com.eduPlazas.eduPlazas.repository.CentroRepository;
import com.eduPlazas.eduPlazas.service.ConvocatoriaService;
import com.eduPlazas.eduPlazas.service.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CentroRepository centroRepository;

    @Autowired
    private SolicitudService solicitudService;

    @Autowired
    private ConvocatoriaService convocatoriaService;

    @Autowired
    private SolicitudRepository solicitudRepository;

    // 1. Mostrar el listado de todas las convocatorias en el Home
    @GetMapping("/home")
    public String home(Model model) {
        List<Convocatoria> todasLasConvocatorias = convocatoriaService.obtenerTodas();
        model.addAttribute("convocatorias", todasLasConvocatorias);

        // Mapa para el conteo de solicitudes por convocatoria
        Map<Long, Long> solicitudesPorConvocatoria = new HashMap<>();
        List<Solicitud> todasLasSolicitudes = solicitudService.obtenerTodas();

        for (Convocatoria conv : todasLasConvocatorias) {
            long count = todasLasSolicitudes.stream()
                .filter(s -> s.getConvocatoria() != null && s.getConvocatoria().getId().equals(conv.getId()))
                .count();
            solicitudesPorConvocatoria.put(conv.getId(), count);
        }

        model.addAttribute("solicitudesPorConvocatoria", solicitudesPorConvocatoria);
        return "admin/home";
    }

    // 2. Mostrar el formulario para crear una convocatoria nueva
    @GetMapping("/convocatoria")
    public String nuevaConvocatoria(Model model) {
        model.addAttribute("nuevaConvocatoria", new Convocatoria());
        
        List<Centro> centros = centroRepository.findAll();
        centros.sort((c1, c2) -> c1.getNombre().compareToIgnoreCase(c2.getNombre()));
        model.addAttribute("centros", centros);
        
        return "admin/convocatoria";
    }

    // 3. Cargar datos de una convocatoria existente para editarla
    @GetMapping("/convocatoria/editar/{id}")
    public String editarConvocatoria(@PathVariable Long id, Model model) {
        Optional<Convocatoria> convocatoriaOpt = convocatoriaService.obtenerPorId(id);
        
        if (convocatoriaOpt.isPresent()) {
            Convocatoria conv = convocatoriaOpt.get();
            // Solo permitimos editar si es Borrador o Activa
            if ("BORRADOR".equals(conv.getEstado()) || "ACTIVA".equals(conv.getEstado())) {
                model.addAttribute("nuevaConvocatoria", conv);
                
                List<Centro> centros = centroRepository.findAll();
                centros.sort((c1, c2) -> c1.getNombre().compareToIgnoreCase(c2.getNombre()));
                model.addAttribute("centros", centros);
                
                return "admin/convocatoria";
            }
        }
        return "redirect:/admin/home";
    }

    // 4. Recibir datos del formulario (Crear o Actualizar)
    @PostMapping("/convocatoria/guardar")
    public String guardarConvocatoria(
            @Valid @ModelAttribute("nuevaConvocatoria") Convocatoria convocatoria,
            BindingResult result,
            @RequestParam(value = "nombresCentrosArray", required = false) String nombresCentros,
            @RequestParam(value = "plazasCentrosArray", required = false) String plazasCentros,
            Model model) {
        
        // 1. Verificación de seguridad de estado (IDOR de estado)
        if (convocatoria.getId() != null) {
            Optional<Convocatoria> existenteOpt = convocatoriaService.obtenerPorId(convocatoria.getId());
            if (existenteOpt.isPresent() && "CERRADA".equals(existenteOpt.get().getEstado())) {
                return "redirect:/admin/home"; // No se puede editar una convocatoria cerrada
            }
        }

        // 2. Validación de backend
        if (result.hasErrors()) {
            List<Centro> centros = centroRepository.findAll();
            centros.sort((c1, c2) -> c1.getNombre().compareToIgnoreCase(c2.getNombre()));
            model.addAttribute("centros", centros);
            return "admin/convocatoria";
        }

        // Primero guardamos la convocatoria
        convocatoriaService.guardarConvocatoria(convocatoria);

        // Lógica de reseteo: Si la convocatoria pasa a estar ACTIVA, reseteamos centros
        if ("ACTIVA".equals(convocatoria.getEstado())) {
            List<Centro> todosLosCentros = centroRepository.findAll();
            for (Centro c : todosLosCentros) {
                c.setNumPlazas(0); // Ponemos a cero para evitar datos residuales
                centroRepository.save(c);
            }
        }

        // Procesamos los arrays de centros y plazas enviados desde el formulario dinámico
        if (nombresCentros != null && !nombresCentros.isEmpty() && plazasCentros != null) {
            String[] nombres = nombresCentros.split(",");
            String[] plazas = plazasCentros.split(",");

            for (int i = 0; i < nombres.length; i++) {
                String nombreCentro = nombres[i].trim();
                
                int plazasDelCentro = 0;
                try {
                    plazasDelCentro = Integer.parseInt(plazas[i].trim());
                } catch (NumberFormatException e) {
                    // Ignoramos el error y dejamos 0 plazas, o manejamos el error de coherencia
                    plazasDelCentro = 0;
                }

                Optional<Centro> centroOpt = centroRepository.findAll().stream()
                        .filter(c -> c.getNombre().equals(nombreCentro))
                        .findFirst();

                if (centroOpt.isPresent()) {
                    Centro centro = centroOpt.get();
                    centro.setNumPlazas(plazasDelCentro);
                    centroRepository.save(centro);
                }
            }
        }

        return "redirect:/admin/home"; 
    }

    // 5. Ver detalle básico de una convocatoria
    @GetMapping("/convocatoria/{id}")
    public String verDetalleConvocatoria(@PathVariable Long id, Model model) {
        Optional<Convocatoria> convocatoriaOpt = convocatoriaService.obtenerPorId(id);

        if (convocatoriaOpt.isEmpty()) {
            return "redirect:/admin/home";
        }

        Convocatoria convocatoria = convocatoriaOpt.get();
        long totalSolicitudes = convocatoriaService.contarSolicitudesPorConvocatoria(convocatoria);

        model.addAttribute("convocatoria", convocatoria);
        model.addAttribute("totalSolicitudes", totalSolicitudes);

        return "admin/convocatoria-detalle";
    }

    // 6. Vista de publicaciones: Estadísticas detalladas y ocupación por centro
    @GetMapping("/convocatoria/{id}/solicitudes")
    public String verSolicitudesPorConvocatoria(@PathVariable Long id, Model model) {
        Optional<Convocatoria> convocatoriaOpt = convocatoriaService.obtenerPorId(id);

        if (convocatoriaOpt.isEmpty()) {
            return "redirect:/admin/home";
        }

        Convocatoria convocatoria = convocatoriaOpt.get();
        List<Solicitud> solicitudes = solicitudRepository.findByConvocatoria(convocatoria);

        // Estadísticas generales
        long totalSolicitudes = solicitudes.size();
        long totalCompletadas = solicitudRepository.countByConvocatoriaAndCompletadaTrue(convocatoria);
        long totalAdmitidas = solicitudRepository.countByConvocatoriaAndEstado(convocatoria, "ADMITIDA");
        long totalListaEspera = solicitudRepository.countByConvocatoriaAndEstado(convocatoria, "LISTA_ESPERA");
        long totalNoAdmitidas = solicitudRepository.countByConvocatoriaAndEstado(convocatoria, "NO_ADMITIDA");

        // Cálculo de ocupación por centro educativo
        List<Centro> centros = centroRepository.findAll();
        centros.sort((c1, c2) -> c1.getNombre().compareToIgnoreCase(c2.getNombre()));

        List<Map<String, Object>> centrosData = new ArrayList<>();
        for (Centro centro : centros) {
            Map<String, Object> centroData = new HashMap<>();
            centroData.put("centro", centro);

            // Contamos solicitudes asignadas a este centro (Admitidas o Enviadas/Pendientes)
            long asignadas = solicitudes.stream()
                    .filter(s -> ("ADMITIDA".equals(s.getEstado()) || "Enviada".equals(s.getEstado())) 
                              && centro.getNombre().equals(s.getCentroPreferencia()))
                    .count();

            int plazasTotales = centro.getNumPlazas() != null ? centro.getNumPlazas() : 0;
            int disponibles = Math.max(0, plazasTotales - (int) asignadas);
            double ocupacion = plazasTotales > 0 ? (asignadas * 100.0) / plazasTotales : 0.0;

            centroData.put("plazasTotales", plazasTotales);
            centroData.put("asignadas", asignadas);
            centroData.put("disponibles", disponibles);
            centroData.put("ocupacion", Math.round(ocupacion * 10.0) / 10.0);
            centrosData.add(centroData);
        }

        model.addAttribute("convocatoria", convocatoria);
        model.addAttribute("totalSolicitudes", totalSolicitudes);
        model.addAttribute("totalCompletadas", totalCompletadas);
        model.addAttribute("totalAdmitidas", totalAdmitidas);
        model.addAttribute("totalListaEspera", totalListaEspera);
        model.addAttribute("totalNoAdmitidas", totalNoAdmitidas);
        model.addAttribute("centrosData", centrosData);

        return "admin/publicaciones";
    }

    @GetMapping("/publicaciones")
    public String publicaciones() {
        return "admin/publicaciones";
    }
}