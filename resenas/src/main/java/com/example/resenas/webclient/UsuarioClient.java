package com.example.resenas.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class UsuarioClient {

    private final WebClient webClient;

    public UsuarioClient(@Value("${usuarios-service.url}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public String obtenerRol(Long idUsuario) {
        try {
            Map<?, ?> response = webClient.get()
                    .uri("/{id}", idUsuario)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null || !response.containsKey("rol")) {
                return null;
            }

            Object rolObj = response.get("rol");
            if (rolObj instanceof Map<?, ?> rolMap) {
                Object nombre = ((Map<?, ?>) rolMap).get("nombre");
                return nombre != null ? nombre.toString() : null;
            }

            return null;
        } catch (Exception e) {
            System.out.println("Error obteniendo rol: " + e.getMessage());
            return null;
        }
    }
}
