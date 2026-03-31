package com.eduPlazas.eduPlazas.repository;

import com.eduPlazas.eduPlazas.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
}