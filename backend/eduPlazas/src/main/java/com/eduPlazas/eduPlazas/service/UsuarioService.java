package com.eduPlazas.eduPlazas.service;

import com.eduPlazas.eduPlazas.model.Usuario;
import com.eduPlazas.eduPlazas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registrarUsuario(Usuario usuario) {
        // Encriptamos la contraseña introducida por el usuario
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        
        // Le asignamos un rol por defecto a todos los que se registran
        usuario.setRol("ROLE_SOLICITANTE");
        
        // Guardamos el usuario en la base de datos H2
        usuarioRepository.save(usuario);
    }
}