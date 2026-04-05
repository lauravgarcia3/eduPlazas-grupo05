package com.eduPlazas.eduPlazas.repository;

import com.eduPlazas.eduPlazas.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

List<Solicitud> findByUsuario(String usuario);

Optional<Solicitud> findByIdAndUsuario(Long id, String usuario);
}