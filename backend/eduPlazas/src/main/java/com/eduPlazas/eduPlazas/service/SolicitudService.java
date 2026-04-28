package com.eduPlazas.eduPlazas.service;

import com.eduPlazas.eduPlazas.model.DocumentoAdjunto;
import com.eduPlazas.eduPlazas.model.Puntuacion;
import com.eduPlazas.eduPlazas.model.Usuario;
import com.eduPlazas.eduPlazas.model.Solicitud;
import com.eduPlazas.eduPlazas.repository.SolicitudRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final PuntuacionService puntuacionService;

    public SolicitudService(SolicitudRepository solicitudRepository,
                            PuntuacionService puntuacionService) {
        this.solicitudRepository = solicitudRepository;
        this.puntuacionService = puntuacionService;
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

        Solicitud guardada = solicitudRepository.save(solicitud);

        double puntosHermanos = Boolean.TRUE.equals(guardada.getTieneHermanosEnCentro()) ? 15.0 : 0.0;
        double puntosProximidad = Boolean.TRUE.equals(guardada.getDomicilioEnZonaCentro()) ? 12.0 : 0.0;
        double puntosFamiliaNumerosa = Boolean.TRUE.equals(guardada.getFamiliaNumerosa()) ? 10.0 : 0.0;
        double puntosFamiliaMonoparental = Boolean.TRUE.equals(guardada.getFamiliaMonoparental()) ? 10.0 : 0.0;
        double puntosDiscapacidad = Boolean.TRUE.equals(guardada.getDiscapacidadAlumnoOTutores()) ? 7.0 : 0.0;
        double puntosRenta = Boolean.TRUE.equals(guardada.getRentaMinimaInsercion()) ? 12.0 : 0.0;

        double puntosTrabajoCentro = 0.0;
        if (guardada.getTutor1() != null &&
                guardada.getTutor1().getSituacionLaboral() != null &&
                (guardada.getTutor1().getSituacionLaboral().equalsIgnoreCase("Trabajando")
                        || guardada.getTutor1().getSituacionLaboral().equalsIgnoreCase("Autónomo"))) {
            puntosTrabajoCentro = 5.0;
        }

        puntuacionService.calcularYGuardar(
                guardada,
                puntosHermanos,
                puntosProximidad,
                puntosTrabajoCentro,
                puntosFamiliaNumerosa,
                puntosFamiliaMonoparental,
                puntosDiscapacidad,
                puntosRenta
        );

        return guardada;
    }

    public Optional<Solicitud> buscarPorId(Long id) {
        return solicitudRepository.findById(id);
    }

    public Optional<Solicitud> obtenerPorId(Long id) {
        return solicitudRepository.findById(id);
    }

    public Optional<Solicitud> buscarPorIdYUsuario(Long id, Usuario usuario) {
        return solicitudRepository.findByIdAndUsuario(id, usuario);
    }

    public Solicitud cambiarEstado(Long id, String estado) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

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

    public double obtenerTotalPuntos(Solicitud solicitud) {
        return puntuacionService.obtenerPorSolicitud(solicitud)
                .map(Puntuacion::getTotalPuntos)
                .orElse(0.0);
    }
}