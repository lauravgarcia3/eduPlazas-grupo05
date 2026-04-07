package com.eduPlazas.eduPlazas.controller;

import com.eduPlazas.eduPlazas.model.Convocatoria;
import com.eduPlazas.eduPlazas.repository.SolicitudRepository;
import com.eduPlazas.eduPlazas.model.Solicitud;
import com.eduPlazas.eduPlazas.model.Centro;
import com.eduPlazas.eduPlazas.service.ConvocatoriaService;
import com.eduPlazas.eduPlazas.service.SolicitudService;
import com.eduPlazas.eduPlazas.repository.CentroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    // Mostrar el listado en el Home
    @GetMapping("/home")
   public String home(Model model) {
        // 1. Obtenemos todas las convocatorias y las pasamos a la vista
        List<Convocatoria> todasLasConvocatorias = convocatoriaService.obtenerTodas();
        model.addAttribute("convocatorias", todasLasConvocatorias);

        // 2. Creamos un mapa para guardar el conteo de cada convocatoria (ID -> Nº de solicitudes)
        Map<Long, Long> solicitudesPorConvocatoria = new HashMap<>();
        List<Solicitud> todasLasSolicitudes = solicitudService.obtenerTodas();

        for (Convocatoria conv : todasLasConvocatorias) {
            long count = todasLasSolicitudes.stream()
                .filter(s -> s.getConvocatoria() != null && s.getConvocatoria().getId().equals(conv.getId()))
                .count();
            solicitudesPorConvocatoria.put(conv.getId(), count);
        }

        // 3. Pasamos el mapa al HTML
        model.addAttribute("solicitudesPorConvocatoria", solicitudesPorConvocatoria);

        return "admin/home";
    }
    // Mostrar el formulario vacío
    @GetMapping("/convocatoria")
    public String convocatoria(Model model) {
        model.addAttribute("nuevaConvocatoria", new Convocatoria());
        
        // Obtenemos los centros y los ordenamos alfabéticamente
        List<Centro> centros = centroRepository.findAll();
        centros.sort((c1, c2) -> c1.getNombre().compareToIgnoreCase(c2.getNombre()));
        
        model.addAttribute("centros", centros);
        
        return "admin/convocatoria";
    }

    // Recibir los datos del formulario y guardar
    @PostMapping("/convocatoria/guardar")
    public String guardarConvocatoria(
            @ModelAttribute("nuevaConvocatoria") Convocatoria convocatoria,
            @RequestParam(value = "nombresCentrosArray", required = false) String nombresCentros,
            @RequestParam(value = "plazasCentrosArray", required = false) String plazasCentros) {
        
        // 1. Guardamos la convocatoria general 
        convocatoriaService.guardarConvocatoria(convocatoria);

        // Si se crea como ACTIVA, reseteamos primero TODOS los colegios a 0 plazas.
        if ("ACTIVA".equals(convocatoria.getEstado())) {
            List<Centro> todosLosCentros = centroRepository.findAll();
            for (Centro c : todosLosCentros) {
                c.setNumPlazas(0);
                centroRepository.save(c);
            }
        }

        // 2. Emparejamos y actualizamos las plazas de cada Centro
        if (nombresCentros != null && !nombresCentros.isEmpty() && plazasCentros != null) {
            String[] nombres = nombresCentros.split(",");
            String[] plazas = plazasCentros.split(",");

            for (int i = 0; i < nombres.length; i++) {
                String nombreCentro = nombres[i].trim();
                int plazasDelCentro = Integer.parseInt(plazas[i].trim());

                // Buscamos el centro por su nombre y le inyectamos las plazas que escribió el Admin
                Optional<Centro> centroOpt = centroRepository.findAll().stream()
                        .filter(c -> c.getNombre().equals(nombreCentro))
                        .findFirst();

                if (centroOpt.isPresent()) {
                    Centro centro = centroOpt.get();
                    centro.setNumPlazas(plazasDelCentro);
                    centroRepository.save(centro); // Actualizamos la BBDD
                }
            }
        }

        return "redirect:/admin/home"; 
    }

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

    @GetMapping("/convocatoria/{id}/solicitudes")
    public String verSolicitudesPorConvocatoria(@PathVariable Long id, Model model) {
        Optional<Convocatoria> convocatoriaOpt = convocatoriaService.obtenerPorId(id);

        if (convocatoriaOpt.isEmpty()) {
            return "redirect:/admin/home";
        }

        Convocatoria convocatoria = convocatoriaOpt.get();

        List<Solicitud> solicitudes = solicitudRepository.findByConvocatoria(convocatoria);
        long totalSolicitudes = solicitudes.size();
        long totalCompletadas = solicitudRepository.countByConvocatoriaAndCompletadaTrue(convocatoria);
        long totalAdmitidas = solicitudRepository.countByConvocatoriaAndEstado(convocatoria, "ADMITIDA");
        long totalListaEspera = solicitudRepository.countByConvocatoriaAndEstado(convocatoria, "LISTA_ESPERA");
        long totalNoAdmitidas = solicitudRepository.countByConvocatoriaAndEstado(convocatoria, "NO_ADMITIDA");

        List<Centro> centros = centroRepository.findAll();
        centros.sort((c1, c2) -> c1.getNombre().compareToIgnoreCase(c2.getNombre()));

        List<Map<String, Object>> centrosData = new java.util.ArrayList<>();
        for (Centro centro : centros) {
            Map<String, Object> centroData = new HashMap<>();
            centroData.put("centro", centro);
            long asignadas = solicitudes.stream()
                    .filter(s -> ("ADMITIDA".equals(s.getEstado()) || "Enviada".equals(s.getEstado())) 
                              && centro.getNombre().equals(s.getCentroPreferencia()))
                    .count();
            int plazasTotales = centro.getNumPlazas() != null ? centro.getNumPlazas() : 0;
            int disponibles = plazasTotales - (int) asignadas;
            if (disponibles < 0) {
                disponibles = 0;
            }
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