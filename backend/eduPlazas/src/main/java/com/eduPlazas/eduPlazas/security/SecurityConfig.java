package com.eduPlazas.eduPlazas.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    @Bean
     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas
                .requestMatchers("/", "/login", "/register", "/css/**", "/images/**", "/h2-console/**").permitAll()
                
                // Rutas protegidas por ROL
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/centro/**").hasRole("CENTRO") 
                .requestMatchers("/solicitante/**").hasRole("SOLICITANTE")
                
                // Cualquier otra ruta requiere estar logueado
                .anyRequest().authenticated()
            )
           
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("email")
                .successHandler(successHandler) 
                .permitAll()
            )
           
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));
        
        return http.build();
    }

    // Este es el "Bean" que pide el UsuarioService para encriptar
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}