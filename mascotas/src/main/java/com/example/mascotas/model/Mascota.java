package com.example.mascotas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "mascotas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa una mascota registrada en el sistema")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la mascota", example = "1")
    private Long id;

    @NotNull
    @Schema(description = "ID del cliente propietario de la mascota", example = "301", required = true)
    private Long idCliente;

    @NotBlank
    @Schema(description = "Nombre de la mascota", example = "Max", required = true)
    private String nombre;

    @Schema(description = "Especie de la mascota (ej. Perro, Gato)", example = "Perro", nullable = true)
    private String especie;

    @Schema(description = "Raza de la mascota (ej. Labrador, Siames)", example = "Labrador", nullable = true)
    private String raza;

    @Schema(description = "Edad de la mascota en años", example = "5", defaultValue = "0", nullable = true)
    private int edad;

    @Lob
    @Column(name = "foto", columnDefinition = "LONGBLOB")
    @Schema(description = "Foto de la mascota en formato BLOB", nullable = true)
    private byte[] foto;
}