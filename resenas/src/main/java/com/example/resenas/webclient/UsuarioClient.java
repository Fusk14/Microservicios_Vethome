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
            System.out.println("🔍 Consultando rol para usuario ID: " + idUsuario);
            Map<?, ?> response = webClient.get()
                    .uri("/{id}", idUsuario)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null) {
                System.out.println("❌ Respuesta nula al consultar usuario ID: " + idUsuario);
                return null;
            }

            if (!response.containsKey("rol")) {
                System.out.println("⚠️ Usuario ID: " + idUsuario + " no tiene campo 'rol'");
                return null;
            }

            Object rolObj = response.get("rol");
            if (rolObj instanceof Map<?, ?> rolMap) {
                Object nombre = rolMap.get("nombre");
                String rolNombre = nombre != null ? nombre.toString() : null;
                System.out.println("✅ Rol obtenido para usuario ID " + idUsuario + ": " + rolNombre);
                return rolNombre;
            }

            System.out.println("⚠️ El campo 'rol' no es un objeto válido para usuario ID: " + idUsuario);
            return null;
        } catch (Exception e) {
            System.out.println("❌ Error obteniendo rol para usuario ID " + idUsuario + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
