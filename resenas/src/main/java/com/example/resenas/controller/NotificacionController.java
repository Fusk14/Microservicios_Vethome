package com.example.resenas.controller;

import com.example.resenas.model.Notificacion;
import com.example.resenas.service.NotificacionService;
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
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "*")
@Tag(name = "Notificaciones", description = "Operaciones de gestión para el envío y consulta de notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService service;

    @Operation(summary = "Enviar una nueva notificación", description = "Crea y envía una notificación a un usuario específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificación enviada exitosamente",
                    content = @Content(schema = @Schema(implementation = Notificacion.class))),
            @ApiResponse(responseCode = "400", description = "Datos de notificación inválidos")
    })
    @PostMapping
    public ResponseEntity<Notificacion> enviar(
            @Parameter(description = "Objeto Notificación a enviar", required = true)
            @Valid @RequestBody Notificacion n) {
        return ResponseEntity.ok(service.enviar(n));
    }

    @Operation(summary = "Listar todas las notificaciones", description = "Obtiene una lista de todas las notificaciones registradas.")
    @ApiResponse(responseCode = "200", description = "Lista de notificaciones",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Notificacion.class))))
    @GetMapping
    public List<Notificacion> listar() {
        return service.listar();
    }

    @Operation(summary = "Obtener una notificación por su ID", description = "Busca y retorna una notificación específica usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificación encontrada",
                    content = @Content(schema = @Schema(implementation = Notificacion.class))),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Notificacion> obtener(
            @Parameter(description = "ID de la notificación a obtener", example = "1", required = true)
            @PathVariable Long id) {
        Notificacion n = service.obtener(id);
        return n != null ? ResponseEntity.ok(n) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Listar notificaciones por ID de usuario", description = "Obtiene todas las notificaciones enviadas a un usuario específico.")
    @ApiResponse(responseCode = "200", description = "Lista de notificaciones para el usuario",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Notificacion.class))))
    @GetMapping("/usuario/{idUsuario}")
    public List<Notificacion> listarPorUsuario(
            @Parameter(description = "ID del usuario para listar sus notificaciones", example = "301", required = true)
            @PathVariable Long idUsuario) {
        return service.listarPorUsuario(idUsuario);
    }

    @Operation(summary = "Eliminar una notificación por su ID", description = "Elimina una notificación específica usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notificación eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada (si el servicio lanza excepción específica)")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la notificación a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}