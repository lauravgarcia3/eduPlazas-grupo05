package com.eduPlazas.eduPlazas.repository;

import com.eduPlazas.eduPlazas.model.Usuario; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Este método es crucial para que el login sepa buscar al usuario por su correo
    Optional<Usuario> findByEmail(String email);
    
}