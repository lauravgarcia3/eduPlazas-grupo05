package com.eduPlazas.eduPlazas.repository;

import com.eduPlazas.eduPlazas.model.Solicitud;
import com.eduPlazas.eduPlazas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    
    List<Solicitud> findByUsuario(Usuario usuario);
    
    Optional<Solicitud> findByIdAndUsuario(Long id, Usuario usuario);

	List<Solicitud> findByCentroPreferencia(String centroPreferencia);
}