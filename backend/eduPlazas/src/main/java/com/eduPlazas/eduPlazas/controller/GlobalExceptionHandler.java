package com.eduPlazas.eduPlazas.controller;

import com.eduPlazas.eduPlazas.repository.CentroRepository;
import com.eduPlazas.eduPlazas.service.ConvocatoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private ConvocatoriaService convocatoriaService;

    @Autowired
    private CentroRepository centroRepository;

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex, Model model) {
        model.addAttribute("fileUploadError", "El tamaño total de los archivos supera el límite permitido. Reduce los documentos o usa archivos más pequeños.");
        cargarDatosFormulario(model);
        return "solicitante/formulario";
    }

    private void cargarDatosFormulario(Model model) {
        var activa = convocatoriaService.obtenerConvocatoriaActiva();
        activa.ifPresent(conv -> {
            model.addAttribute("convocatoriaActiva", conv);
            if (conv.getFechaInicio() != null && conv.getFechaFin() != null) {
                var formatter = java.time.format.DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new java.util.Locale("es", "ES"));
                model.addAttribute("fechaInicioFormat", conv.getFechaInicio().format(formatter));
                model.addAttribute("fechaFinFormat", conv.getFechaFin().format(formatter));
            }
        });

        var centros = centroRepository.findAll();
        centros.sort((c1, c2) -> c1.getNombre().compareToIgnoreCase(c2.getNombre()));
        model.addAttribute("centros", centros);
    }
}
