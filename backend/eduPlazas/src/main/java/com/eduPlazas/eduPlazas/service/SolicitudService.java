package com.eduPlazas.eduPlazas.service;

import com.eduPlazas.eduPlazas.model.DocumentoAdjunto;
import com.eduPlazas.eduPlazas.model.Puntuacion;
import com.eduPlazas.eduPlazas.model.Usuario;
import com.eduPlazas.eduPlazas.model.Solicitud;
import com.eduPlazas.eduPlazas.model.Centro;
import com.eduPlazas.eduPlazas.model.Convocatoria;
import com.eduPlazas.eduPlazas.repository.SolicitudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

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
        
        // Usamos el motor centralizado
        calcularYGuardarBaremacion(guardada);
        return guardada;
    }

    // MÉTODO CENTRALIZADO PARA EL PUNTO 9
    public void calcularYGuardarBaremacion(Solicitud guardada) {
        double puntosHermanos = Boolean.TRUE.equals(guardada.getTieneHermanosEnCentro()) ? 15.0 : 0.0;
        double puntosProximidad = Boolean.TRUE.equals(guardada.getDomicilioEnZonaCentro()) ? 12.0 : 0.0;
        double puntosFamiliaNumerosa = Boolean.TRUE.equals(guardada.getFamiliaNumerosa()) ? 10.0 : 0.0;
        double puntosFamiliaMonoparental = Boolean.TRUE.equals(guardada.getFamiliaMonoparental()) ? 8.0 : 0.0;
        double puntosDiscapacidad = Boolean.TRUE.equals(guardada.getDiscapacidadAlumnoOTutores()) ? 6.0 : 0.0;
        double puntosRenta = Boolean.TRUE.equals(guardada.getRentaMinimaInsercion()) ? 4.0 : 0.0;

        double puntosTrabajoCentro = 0.0;
        if (guardada.getTutor1() != null && guardada.getTutor1().getSituacionLaboral() != null &&
            (guardada.getTutor1().getSituacionLaboral().equalsIgnoreCase("Trabajando") || 
             guardada.getTutor1().getSituacionLaboral().equalsIgnoreCase("Autónomo"))) {
            puntosTrabajoCentro = 5.0;
        }

        double puntosVictimaViolencia = Boolean.TRUE.equals(guardada.getVictimaViolenciaGenero()) ? 10.0 : 0.0;
        double puntosConciliacion = Boolean.TRUE.equals(guardada.getConciliacionLaboral()) ? 3.0 : 0.0;
        double puntosTraslado = Boolean.TRUE.equals(guardada.getTrasladoFamiliar()) ? 2.0 : 0.0;

        puntuacionService.calcularYGuardar(guardada, puntosHermanos, puntosProximidad, puntosTrabajoCentro, 
            puntosFamiliaNumerosa, puntosFamiliaMonoparental, puntosDiscapacidad, puntosRenta, 
            puntosVictimaViolencia, puntosConciliacion, puntosTraslado);
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

        List<String> estadosOficiales = List.of("BORRADOR", "ENVIADA", "ADMITIDA", "LISTA_ESPERA", "NO_ADMITIDA");

        String estadoUpper = estado.toUpperCase();

        if (!estadosOficiales.contains(estadoUpper)) {
            throw new RuntimeException("Estado inválido. Use uno de los estados del SDD: " + estadosOficiales);
        }

        solicitud.setEstado(estadoUpper);
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

    public List<Solicitud> obtenerSolicitudesOrdenadasPorPuntuacion(Convocatoria convocatoria) {
        List<Solicitud> solicitudes = new ArrayList<>(solicitudRepository.findByConvocatoria(convocatoria));

        return solicitudes.stream()
                .filter(this::esSolicitudAdjudicable)
                .sorted(comparadorSolicitudesPorBaremo())
                .toList();
    }

    @Transactional
    public void adjudicarSolicitudes(Convocatoria convocatoria, List<Centro> centros) {
        Map<String, Integer> plazasDisponiblesPorCentro = construirMapaPlazas(centros);
        List<Solicitud> solicitudesOrdenadas = obtenerSolicitudesOrdenadasPorPuntuacion(convocatoria);

        for (Solicitud solicitud : solicitudesOrdenadas) {
            solicitud.setCentroAdjudicado(null);
            boolean adjudicada = false;

            for (String centroSolicitado : obtenerPreferenciasCentro(solicitud)) {
                String centroNormalizado = normalizarCentro(centroSolicitado);
                int plazasDisponibles = plazasDisponiblesPorCentro.getOrDefault(centroNormalizado, 0);

                if (plazasDisponibles > 0) {
                    solicitud.setEstado("ADMITIDA");
                    solicitud.setCentroAdjudicado(centroSolicitado);
                    plazasDisponiblesPorCentro.put(centroNormalizado, plazasDisponibles - 1);
                    adjudicada = true;
                    break;
                }
            }

            if (!adjudicada) {
                solicitud.setEstado("LISTA_ESPERA");
            }

            solicitudRepository.save(solicitud);
        }
    }

    private boolean esSolicitudAdjudicable(Solicitud solicitud) {
        if (solicitud == null)
            return false;

        String estado = solicitud.getEstado();

        return Boolean.TRUE.equals(solicitud.getCompletada())
                || "Enviada".equalsIgnoreCase(estado)
                || "Aceptada".equalsIgnoreCase(estado);
    }

    private Comparator<Solicitud> comparadorSolicitudesPorBaremo() {
        return Comparator
                .comparingDouble(this::obtenerTotalPuntos).reversed()
                .thenComparing((Solicitud s) -> Boolean.TRUE.equals(s.getTieneHermanosEnCentro()) ? 0 : 1)
                .thenComparing((Solicitud s) -> Boolean.TRUE.equals(s.getDomicilioEnZonaCentro()) ? 0 : 1)
                .thenComparing(s -> s.getId() != null ? s.getId() : Long.MAX_VALUE);
    }

    private Map<String, Integer> construirMapaPlazas(List<Centro> centros) {
        Map<String, Integer> plazas = new HashMap<>();

        if (centros == null)
            return plazas;

        for (Centro centro : centros) {
            String nombreCentro = normalizarCentro(centro.getNombre());
            int numPlazas = centro.getNumPlazas() != null ? centro.getNumPlazas() : 0;
            plazas.put(nombreCentro, plazas.getOrDefault(nombreCentro, 0) + Math.max(numPlazas, 0));
        }

        return plazas;
    }

    private String normalizarCentro(String centro) {
        return centro == null ? "" : centro.trim().toLowerCase();
    }

    public List<String> obtenerPreferenciasCentro(Solicitud solicitud) {
        List<String> preferencias = new ArrayList<>();

        if (solicitud == null)
            return preferencias;

        agregarPreferencia(preferencias, solicitud.getCentroPreferencia1());
        agregarPreferencia(preferencias, solicitud.getCentroPreferencia2());
        agregarPreferencia(preferencias, solicitud.getCentroPreferencia3());

        return preferencias;
    }

    private void agregarPreferencia(List<String> preferencias, String centro) {
        if (estaVacio(centro))
            return;

        String centroNormalizado = normalizarCentro(centro);
        boolean yaIncluido = preferencias.stream()
                .map(this::normalizarCentro)
                .anyMatch(centroNormalizado::equals);

        if (!yaIncluido) {
            preferencias.add(centro.trim());
        }
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}
