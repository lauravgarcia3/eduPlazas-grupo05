package com.eduPlazas.eduPlazas.security;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class CustomAuthenticationSuccessHandlerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CustomAuthenticationSuccessHandler successHandler;

    @Test
    void testRedireccionAdmin() throws IOException, ServletException {
        when(authentication.getName()).thenReturn("Admin");
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))).when(authentication).getAuthorities();

        successHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response).sendRedirect("/admin/home");
    }

    @Test
    void testRedireccionSolicitante() throws IOException, ServletException {
        when(authentication.getName()).thenReturn("Familia");
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_SOLICITANTE"))).when(authentication).getAuthorities();

        successHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response).sendRedirect("/solicitante/home");
    }

    @Test
    void testRedireccionCentro() throws IOException, ServletException {
        when(authentication.getName()).thenReturn("Colegio");
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_CENTRO"))).when(authentication).getAuthorities();

        successHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response).sendRedirect("/centro/home");
    }
}