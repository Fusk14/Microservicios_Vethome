package com.example.consultas.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class MascotaClient {

    private final WebClient webClient;

    public MascotaClient(@Value("${mascota-service.url}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public boolean existeMascota(Long id) {
        try {
            Map response = webClient.get()
                    .uri("/{id}", id)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            return response != null;
        } catch (Exception e) {
            return false;
        }
    }
}
