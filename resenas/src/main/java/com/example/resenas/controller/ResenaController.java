package com.example.resenas.controller;

import com.example.resenas.model.Resena;
import com.example.resenas.service.ResenaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resenas")
@CrossOrigin(origins = "*")
@Tag(name = "Reseñas", description = "Operaciones de gestión para reseñas y calificaciones de servicios")
public class ResenaController {

    @Autowired
    private ResenaService service;

    @Operation(summary = "Crear una nueva reseña", description = "Registra una nueva reseña o calificación de un servicio veterinario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reseña creada exitosamente",
                    content = @Content(schema = @Schema(implementation = Resena.class))),
            @ApiResponse(responseCode = "400", description = "Datos de reseña inválidos o error de negocio")
    })
    @PostMapping
    public ResponseEntity<?> crear(
            @Parameter(description = "Objeto Reseña a crear", required = true)
            @Valid @RequestBody Resena resena) {
        try {
            return ResponseEntity.ok(service.guardar(resena));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Listar todas las reseñas", description = "Obtiene una lista de todas las reseñas registradas.")
    @ApiResponse(responseCode = "200", description = "Lista de reseñas",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Resena.class))))
    @GetMapping
    public List<Resena> listar() {
        return service.listar();
    }
}