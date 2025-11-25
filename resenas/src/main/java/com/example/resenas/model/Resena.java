package com.example.resenas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "resenas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa una reseña o calificación de un servicio veterinario")
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long idCliente;

    @NotNull
    private Long idVeterinario;

    @Min(1)
    @Max(5)
    private int calificacion;

    @NotBlank
    private String comentario;
}
