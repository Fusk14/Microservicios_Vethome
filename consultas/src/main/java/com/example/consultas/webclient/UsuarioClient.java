package com.example.consultas.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
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
            if (response != null && response.get("rol") != null) {
                Map<?, ?> rol = (Map<?, ?>) response.get("rol");
                if (rol != null && rol.get("nombre") != null) {
                    return rol.get("nombre").toString();
                }
            }
            return null;
        } catch (Exception e) {
            System.err.println("Error al obtener rol del usuario " + idUsuario + ": " + e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public Long obtenerPrimerVeterinario() {
        try {
            List<Map<?, ?>> usuarios = (List<Map<?, ?>>) webClient.get()
                    .uri("")
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();
            
            if (usuarios != null) {
                for (Map<?, ?> usuario : usuarios) {
                    Object rolObj = usuario.get("rol");
                    if (rolObj != null) {
                        Map<?, ?> rol = (Map<?, ?>) rolObj;
                        Object nombreRol = rol.get("nombre");
                        if (nombreRol != null && "VETERINARIO".equalsIgnoreCase(nombreRol.toString())) {
                            Object idObj = usuario.get("id");
                            if (idObj instanceof Number) {
                                return ((Number) idObj).longValue();
                            }
                        }
                    }
                }
            }
            System.err.println("No se encontró ningún veterinario en el sistema");
            return null;
        } catch (Exception e) {
            System.err.println("Error al obtener veterinarios: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
