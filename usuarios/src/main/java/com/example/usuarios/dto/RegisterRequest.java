package com.example.usuarios.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String rut;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String contrasena;
    private String rolNombre; // "CLIENTE" | "VETERINARIO" | "ADMINISTRATIVO"
}
