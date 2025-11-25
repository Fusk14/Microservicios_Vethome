package com.example.usuarios.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa un rol de usuario (Cliente, Veterinario, Administrativo)")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del rol", example = "1")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Nombre del rol. Puede ser CLIENTE, VETERINARIO o ADMINISTRATIVO", example = "CLIENTE", required = true)
    private String nombre;
}
