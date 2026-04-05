package com.eduPlazas.eduPlazas.service;

import com.eduPlazas.eduPlazas.model.DocumentoAdjunto;
import com.eduPlazas.eduPlazas.model.Usuario;
import com.eduPlazas.eduPlazas.model.Solicitud;
import com.eduPlazas.eduPlazas.repository.SolicitudRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SolicitudService {

private final SolicitudRepository solicitudRepository;

public SolicitudService(SolicitudRepository solicitudRepository) {
this.solicitudRepository = solicitudRepository;
}

public List<Solicitud> obtenerTodas() {
return solicitudRepository.findAll();
}

public List<Solicitud> obtenerPorUsuario(Usuario usuario) {
return solicitudRepository.findByUsuario(usuario);
}

public List<Solicitud> obtenerIncompletasPorUsuario(Usuario usuario) {
    return solicitudRepository.findByUsuario(usuario).stream()
            .filter(solicitud -> !solicitud.getCompletada())
            .toList();
}

public Solicitud guardar(Solicitud solicitud) {
if (solicitud.getDocumentos() != null) {
for (DocumentoAdjunto documento : solicitud.getDocumentos()) {
documento.setSolicitud(solicitud);
}
}
return solicitudRepository.save(solicitud);
}

public Optional<Solicitud> buscarPorId(Long id) {
return solicitudRepository.findById(id);
}

public Optional<Solicitud> buscarPorIdYUsuario(Long id, Usuario usuario) {
return solicitudRepository.findByIdAndUsuario(id, usuario);
}

public Solicitud cambiarEstado(Long id, String estado) {

Solicitud solicitud = solicitudRepository.findById(id)
.orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

// Validación básica (opcional pero recomendable)
if (!estado.equals("Pendiente") && !estado.equals("Aceptada") && !estado.equals("Rechazada")) {
throw new RuntimeException("Estado inválido");
}

solicitud.setEstado(estado);
return solicitudRepository.save(solicitud);
}

public Solicitud actualizarEstado(Long id, boolean completada) {
    Optional<Solicitud> solicitudOpt = solicitudRepository.findById(id);
    if (solicitudOpt.isPresent()) {
        Solicitud solicitud = solicitudOpt.get();
        solicitud.setCompletada(completada);
        return solicitudRepository.save(solicitud);
    }
    throw new IllegalArgumentException("Solicitud no encontrada");
}
}