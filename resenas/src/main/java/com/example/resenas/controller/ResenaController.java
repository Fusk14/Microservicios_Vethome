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
            @ApiResponse(responseCode = "201", description = "Reseña creada exitosamente",
                    content = @Content(schema = @Schema(implementation = Resena.class))),
            @ApiResponse(responseCode = "400", description = "Datos de reseña inválidos o error de negocio (calificación fuera de rango, comentario vacío, rol inválido)"),
            @ApiResponse(responseCode = "404", description = "Cliente o veterinario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<?> crear(
            @Parameter(description = "Objeto Reseña a crear", required = true)
            @Valid @RequestBody Resena resena) {
        try {
            System.out.println("📥 Recibiendo solicitud para crear reseña:");
            System.out.println("   - ID Cliente: " + resena.getIdCliente());
            System.out.println("   - ID Veterinario: " + resena.getIdVeterinario());
            System.out.println("   - Calificación: " + resena.getCalificacion());
            System.out.println("   - Comentario: " + resena.getComentario());
            
            Resena guardada = service.guardar(resena);
            System.out.println("✅ Reseña creada exitosamente con ID: " + guardada.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error de validación: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error inesperado al crear reseña: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la reseña: " + e.getMessage());
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