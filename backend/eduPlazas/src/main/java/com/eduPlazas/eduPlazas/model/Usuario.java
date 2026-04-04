package com.eduPlazas.eduPlazas.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data // Lombok: Genera Getters, Setters, toString y equals automáticamente
@NoArgsConstructor // Lombok: Genera el constructor vacío exigido por JPA
@AllArgsConstructor // Lombok: Genera un constructor con todos los campos
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nombreCompleto;

    @Column(nullable = false)
    private String rol;
}