package com.example.consultas.model;

import io.swagger.v3.oas.annotations.media.Schema; 
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "consultas")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa una consulta veterinaria")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la consulta", example = "1")
    private Long id;

    @NotNull
    @Schema(description = "ID de la mascota asociada a la consulta", example = "101", required = true)
    private Long idMascota;

    @NotNull
    @Schema(description = "ID del veterinario que atiende la consulta", example = "201", required = true)
    private Long idVeterinario;

    @NotNull
    @Schema(description = "ID del cliente propietario de la mascota", example = "301", required = true)
    private Long idCliente;

    @NotNull
    @Schema(description = "Fecha de la consulta (YYYY-MM-DD)", example = "2025-07-15", required = true)
    private LocalDate fecha;

    @Schema(description = "Motivo de la consulta", example = "Revisión anual", nullable = true)
    private String motivo;

    @Schema(description = "Diagnóstico realizado durante la consulta", example = "Otitis leve", nullable = true)
    private String diagnostico;

    @Schema(description = "Tratamiento prescrito para la mascota", example = "Antibiótico por 7 días", nullable = true)
    private String tratamiento;
}