package com.eduPlazas.eduPlazas.service;

import com.eduPlazas.eduPlazas.model.Convocatoria;
import com.eduPlazas.eduPlazas.repository.ConvocatoriaRepository;
import com.eduPlazas.eduPlazas.repository.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConvocatoriaService {

    @Autowired
    private ConvocatoriaRepository repository;

    @Autowired
    private SolicitudRepository solicitudRepository;

    public void guardarConvocatoria(Convocatoria nuevaConvocatoria) {
        // Si el admin dice que esta nueva va a estar ACTIVA, quitamos la que esté ya como activa
        if ("ACTIVA".equals(nuevaConvocatoria.getEstado())) {
            // Buscamos si ya había otra activa en la base de datos
            Optional<Convocatoria> activaAnterior = repository.findByEstado("ACTIVA");

            // Si existía, la apagamos (la ponemos en CERRADA) y la guardamos
            if (activaAnterior.isPresent()) {
                Convocatoria anterior = activaAnterior.get();
                anterior.setEstado("CERRADA");
                repository.save(anterior);
            }
        }

        if (nuevaConvocatoria.getId() != null) {
            Optional<Convocatoria> existenteOpt = repository.findById(nuevaConvocatoria.getId());
            if (existenteOpt.isPresent()) {
                Convocatoria existente = existenteOpt.get();
                if (nuevaConvocatoria.getListadoProvisionalPublicado() == null) {
                    nuevaConvocatoria.setListadoProvisionalPublicado(existente.getListadoProvisionalPublicado());
                }
                if (nuevaConvocatoria.getReclamacionesFinalizadas() == null) {
                    nuevaConvocatoria.setReclamacionesFinalizadas(existente.getReclamacionesFinalizadas());
                }
                if (nuevaConvocatoria.getReclamacionesProcesadas() == null) {
                    nuevaConvocatoria.setReclamacionesProcesadas(existente.getReclamacionesProcesadas());
                }
                if (nuevaConvocatoria.getListadoDefinitivoPublicado() == null) {
                    nuevaConvocatoria.setListadoDefinitivoPublicado(existente.getListadoDefinitivoPublicado());
                }
                if (nuevaConvocatoria.getPeriodoReclamacionesFinalizado() == null) {
                    nuevaConvocatoria.setPeriodoReclamacionesFinalizado(existente.getPeriodoReclamacionesFinalizado());
                }
                if (nuevaConvocatoria.getReclamacionesDefinitivasProcesadas() == null) {
                    nuevaConvocatoria.setReclamacionesDefinitivasProcesadas(existente.getReclamacionesDefinitivasProcesadas());
                }
            }
        }

        // Finalmente, guardamos la nueva
        repository.save(nuevaConvocatoria);
    }

    public List<Convocatoria> obtenerTodas() {
        return repository.findAllOrdered();
    }

    public Optional<Convocatoria> obtenerConvocatoriaActiva() {
        return repository.findByEstado("ACTIVA");
    }

    public Optional<Convocatoria> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    public long contarSolicitudesPorConvocatoria(Convocatoria convocatoria) {
        return solicitudRepository.countByConvocatoria(convocatoria);
    }
}