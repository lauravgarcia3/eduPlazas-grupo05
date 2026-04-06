package com.eduPlazas.eduPlazas.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.eduPlazas.eduPlazas.model.Puntuacion;
import com.eduPlazas.eduPlazas.model.Solicitud;
import com.eduPlazas.eduPlazas.repository.PuntuacionRepository;

@Service
public class PuntuacionService {

    private final PuntuacionRepository puntuacionRepository;

    public PuntuacionService(PuntuacionRepository puntuacionRepository) {
        this.puntuacionRepository = puntuacionRepository;
    }

    public Puntuacion calcularYGuardar(Solicitud solicitud,
                                       double puntosHermanos,
                                       double puntosProximidad,
                                       double puntosTrabajoCentro,
                                       double puntosFamiliaNumerosa,
                                       double puntosFamiliaMonoparental,
                                       double puntosDiscapacidad,
                                       double puntosRenta) {

        Optional<Puntuacion> existenteOpt = puntuacionRepository.findBySolicitud(solicitud);
        Puntuacion p = existenteOpt.orElseGet(Puntuacion::new);

        p.setSolicitud(solicitud);

        p.setPuntosHermanos(puntosHermanos);
        p.setPuntosProximidad(puntosProximidad);
        p.setPuntosTrabajoCentro(puntosTrabajoCentro);
        p.setPuntosFamiliaNumerosa(puntosFamiliaNumerosa);
        p.setPuntosFamiliaMonoparental(puntosFamiliaMonoparental);
        p.setPuntosDiscapacidad(puntosDiscapacidad);
        p.setPuntosRenta(puntosRenta);

        double total = puntosHermanos
                     + puntosProximidad
                     + puntosTrabajoCentro
                     + puntosFamiliaNumerosa
                     + puntosFamiliaMonoparental
                     + puntosDiscapacidad
                     + puntosRenta;

        p.setTotalPuntos(total);
        p.setFechaCalculo(LocalDateTime.now());

        return puntuacionRepository.save(p);
    }

    public Optional<Puntuacion> obtenerPorSolicitud(Solicitud solicitud) {
        return puntuacionRepository.findBySolicitud(solicitud);
    }
}