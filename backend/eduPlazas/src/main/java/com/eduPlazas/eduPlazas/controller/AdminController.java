package com.eduPlazas.eduPlazas.controller;

import com.eduPlazas.eduPlazas.model.Convocatoria;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String guardarConvocatoria(@ModelAttribute("nuevaConvocatoria") Convocatoria convocatoria) {
        convocatoriaService.guardarConvocatoria(convocatoria);
        return "redirect:/admin/home"; // Le mandamos al home para que vea su nueva convocatoria
    }

    @GetMapping("/publicaciones")
    public String publicaciones() {
        return "admin/publicaciones";
    }
}


