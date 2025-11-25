package com.example.consultas.controller;

import com.example.consultas.model.Consulta;
import com.example.consultas.service.ConsultaService;
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
@RequestMapping("/api/consultas")
@CrossOrigin(origins = "*")
@Tag(name = "Consultas", description = "Operaciones de gestión para consultas veterinarias")
public class ConsultaController {

    @Autowired
    private ConsultaService service;

    // Endpoint de prueba para verificar que el controller funciona
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Consultas service is working!");
    }

    @Operation(summary = "Listar todas las consultas", description = "Obtiene una lista de todas las consultas registradas.")
    @ApiResponse(responseCode = "200", description = "Lista de consultas",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Consulta.class))))
    @GetMapping
    public List<Consulta> listar() {
        return service.listar();
    }

    @Operation(summary = "Obtener una consulta por su ID", description = "Busca y retorna una consulta específica usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta encontrada",
                    content = @Content(schema = @Schema(implementation = Consulta.class))),
            @ApiResponse(responseCode = "404", description = "Consulta no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Consulta> obtener(
            @Parameter(description = "ID de la consulta a obtener", example = "1", required = true)
            @PathVariable Long id) {
        Consulta consulta = service.obtener(id);
        return consulta != null ? ResponseEntity.ok(consulta) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear una nueva consulta", description = "Registra una nueva consulta en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta creada exitosamente",
                    content = @Content(schema = @Schema(implementation = Consulta.class))),
            @ApiResponse(responseCode = "400", description = "Datos de consulta inválidos o error de negocio")
    })
    @PostMapping
    public ResponseEntity<?> crear(
            @Parameter(description = "Objeto Consulta a crear", required = true)
            @Valid @RequestBody Consulta consulta) {
        try {
            return ResponseEntity.ok(service.guardar(consulta));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Eliminar una consulta por su ID", description = "Elimina una consulta específica usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Consulta eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Consulta no encontrada (si el servicio lanza excepción específica)")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la consulta a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}