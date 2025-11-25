package com.example.usuarios.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa a un usuario del sistema")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del usuario", example = "1")
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    @Schema(description = "RUT del usuario. Debe ser único", example = "12345678-9", required = true)
    private String rut;

    @NotBlank
    @Schema(description = "Nombre del usuario", example = "Juan", required = true)
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Pérez")
    @Column(nullable = true)
    private String apellido;

    @Email
    @NotBlank
    @Schema(description = "Correo electrónico del usuario", example = "juan@example.com", required = true)
    private String correo;

    @NotBlank
    @Schema(description = "Número de teléfono del usuario", example = "+56912345678", required = true)
    private String telefono;

    @NotBlank
    @Schema(description = "Contraseña del usuario (almacenada encriptada)", example = "secreta123", required = true)
    private String contrasena;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    @Schema(description = "Rol asignado al usuario", implementation = Rol.class)
    private Rol rol;
}
