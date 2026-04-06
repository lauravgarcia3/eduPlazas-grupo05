package com.eduPlazas.eduPlazas.controller;

import com.eduPlazas.eduPlazas.model.Convocatoria;
import com.eduPlazas.eduPlazas.repository.SolicitudRepository;
import com.eduPlazas.eduPlazas.service.ConvocatoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ConvocatoriaService convocatoriaService;

    @Autowired
    private SolicitudRepository solicitudRepository;

    // Mostrar el listado en el Home
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("convocatorias", convocatoriaService.obtenerTodas());
        return "admin/home";
    }

    // Mostrar el formulario vacío
    @GetMapping("/convocatoria")
    public String convocatoria(Model model) {
        model.addAttribute("nuevaConvocatoria", new Convocatoria());
        return "admin/convocatoria";
    }

    // Recibir los datos del formulario y guardar
    @PostMapping("/convocatoria/guardar")
    public String guardarConvocatoria(@ModelAttribute("nuevaConvocatoria") Convocatoria convocatoria) {
        convocatoriaService.guardarConvocatoria(convocatoria);
        return "redirect:/admin/home"; // Le mandamos al home para que vea su nueva convocatoria
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

        long totalSolicitudes = solicitudRepository.countByConvocatoria(convocatoria);
        long totalCompletadas = solicitudRepository.countByConvocatoriaAndCompletadaTrue(convocatoria);
        long totalAdmitidas = solicitudRepository.countByConvocatoriaAndEstado(convocatoria, "ADMITIDA");
        long totalListaEspera = solicitudRepository.countByConvocatoriaAndEstado(convocatoria, "LISTA_ESPERA");
        long totalNoAdmitidas = solicitudRepository.countByConvocatoriaAndEstado(convocatoria, "NO_ADMITIDA");

        model.addAttribute("convocatoria", convocatoria);
        model.addAttribute("totalSolicitudes", totalSolicitudes);
        model.addAttribute("totalCompletadas", totalCompletadas);
        model.addAttribute("totalAdmitidas", totalAdmitidas);
        model.addAttribute("totalListaEspera", totalListaEspera);
        model.addAttribute("totalNoAdmitidas", totalNoAdmitidas);

        return "admin/publicaciones";
    }

    @GetMapping("/publicaciones")
    public String publicaciones() {
        return "admin/publicaciones";
    }
}