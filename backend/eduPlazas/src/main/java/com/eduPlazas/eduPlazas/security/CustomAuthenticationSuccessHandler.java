package com.eduPlazas.eduPlazas.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

@Override
public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

System.out.println("=== LOGIN EXITOSO ===");
System.out.println("Usuario logueado: " + authentication.getName());
System.out.println("Roles detectados: " + authentication.getAuthorities());

<<<<<<< HEAD
String redirectUrl = "/";
=======
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String role = authority.getAuthority();
            
            if (role.equals("ROLE_ADMIN")) {
                System.out.println("-> Detectado como ADMIN. Redirigiendo a /admin/home");
                redirectUrl = "/admin/home";
                break;
            } else if (role.equals("ROLE_SOLICITANTE")) {
                System.out.println("-> Detectado como SOLICITANTE. Redirigiendo a /solicitante/home");
                redirectUrl = "/solicitante/home";
                break;
                } else if (role.equals("ROLE_CENTRO")) {
                System.out.println("-> Detectado como CENTRO. Redirigiendo a /centro/home");
                redirectUrl = "/centro/home";
            }
        }
>>>>>>> main

for (GrantedAuthority authority : authentication.getAuthorities()) {
String role = authority.getAuthority();

if (role.equals("ROLE_ADMIN")) {
System.out.println("-> Detectado como ADMIN. Redirigiendo a /admin/home");
redirectUrl = "/admin/home";
break;
} else if (role.equals("ROLE_SOLICITANTE")) {
System.out.println("-> Detectado como SOLICITANTE. Redirigiendo a /solicitante/home");
redirectUrl = "/solicitante/home";
break;
} else if (role.equals("ROLE_CENTRO")) {
System.out.println("-> Detectado como CENTRO. Redirigiendo a /centro/home");
redirectUrl = "/centro/home";
break;
}
}

System.out.println("-> Destino final: " + redirectUrl);
response.sendRedirect(redirectUrl);
}
}