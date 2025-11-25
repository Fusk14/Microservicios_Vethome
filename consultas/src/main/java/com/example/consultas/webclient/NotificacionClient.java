package com.example.consultas.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class NotificacionClient {

    private final WebClient webClient;

    public NotificacionClient(@Value("${notificacion-service.url}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public void enviarNotificacion(Long idCliente, String mensaje) {
        try {
            webClient.post()
                .uri(uriBuilder -> uriBuilder
                    .queryParam("idCliente", idCliente)
                    .queryParam("mensaje", mensaje)
                    .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block(); // simple para entorno académico
        } catch (Exception e) {
            // No lanzar excepción para que no afecte el guardado de la consulta
            System.err.println("Error al enviar notificación: " + e.getMessage());
            throw e; // Re-lanzar para que el servicio pueda manejarlo
        }
    }
}
