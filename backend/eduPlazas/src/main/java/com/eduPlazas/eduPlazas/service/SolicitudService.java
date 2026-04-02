package com.eduPlazas.eduPlazas.service;

import com.eduPlazas.eduPlazas.model.DocumentoAdjunto;
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
}
