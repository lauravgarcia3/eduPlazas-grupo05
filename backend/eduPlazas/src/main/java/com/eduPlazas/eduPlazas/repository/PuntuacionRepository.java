package com.eduPlazas.eduPlazas.repository;

import com.eduPlazas.eduPlazas.model.Puntuacion;
import com.eduPlazas.eduPlazas.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PuntuacionRepository extends JpaRepository<Puntuacion, Long> {
    Optional<Puntuacion> findBySolicitud(Solicitud solicitud);
}