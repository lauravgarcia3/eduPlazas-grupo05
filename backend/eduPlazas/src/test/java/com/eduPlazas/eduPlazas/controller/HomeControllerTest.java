package com.eduPlazas.eduPlazas.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class HomeControllerTest {

    @Test
    void testHomeRedirigeALogin() {
        // 1. Instanciamos el controlador directamente (sin librerías web)
        HomeController homeController = new HomeController();

        // 2. Ejecutamos su método
        String vista = homeController.home();

        // 3. Comprobamos con AssertJ que nos devuelve el texto de redirección correcto
        assertThat(vista).isEqualTo("redirect:/login");
    }
}