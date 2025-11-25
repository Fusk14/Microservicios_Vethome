package com.example.mascotas.webclient;

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

    public String obtenerRolPorId(Long idUsuario) {
        try {
            Map<?, ?> response = webClient.get()
                    .uri("/{id}", idUsuario)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            return response != null ? ((Map<?, ?>) response.get("rol")).get("nombre").toString() : null;
        } catch (Exception e) {
            return null;
        }
    }
}
