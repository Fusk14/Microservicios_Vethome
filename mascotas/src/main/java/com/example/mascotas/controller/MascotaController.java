package com.example.mascotas.controller;

import com.example.mascotas.model.Mascota;
import com.example.mascotas.service.MascotaService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/mascotas")
@CrossOrigin(origins = "*")
@Tag(name = "Mascotas", description = "Operaciones de gestión para mascotas")
public class MascotaController {

    @Autowired
    private MascotaService service;

    @Operation(summary = "Crear una nueva mascota", description = "Registra una nueva mascota en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mascota creada exitosamente",
                    content = @Content(schema = @Schema(implementation = Mascota.class))),
            @ApiResponse(responseCode = "400", description = "Datos de mascota inválidos o error de negocio")
    })
    @PostMapping
    public ResponseEntity<?> crear(
            @Parameter(description = "Objeto Mascota a crear", required = true)
            @Valid @RequestBody Mascota mascota) {
        try {
            return ResponseEntity.ok(service.guardar(mascota));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Listar todas las mascotas", description = "Obtiene una lista de todas las mascotas registradas.")
    @ApiResponse(responseCode = "200", description = "Lista de mascotas",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Mascota.class))))
    @GetMapping
    public List<Mascota> listar() {
        return service.listar();
    }

    @Operation(summary = "Obtener una mascota por su ID", description = "Busca y retorna una mascota específica usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mascota encontrada",
                    content = @Content(schema = @Schema(implementation = Mascota.class))),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Mascota> obtener(
            @Parameter(description = "ID de la mascota a obtener", example = "1", required = true)
            @PathVariable Long id) {
        Mascota mascota = service.obtener(id);
        return mascota != null ? ResponseEntity.ok(mascota) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar una mascota por su ID", description = "Elimina una mascota específica usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Mascota eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada (si el servicio lanza excepción específica)")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la mascota a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Subir foto de una mascota", description = "Sube una imagen para una mascota específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Foto subida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Archivo inválido o mascota no encontrada"),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada")
    })
    @PostMapping("/{id}/foto")
    public ResponseEntity<?> subirFoto(
            @Parameter(description = "ID de la mascota", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "Archivo de imagen", required = true)
            @RequestParam("foto") MultipartFile foto) {
        try {
            if (foto.isEmpty()) {
                return ResponseEntity.badRequest().body("El archivo está vacío");
            }
            if (!foto.getContentType().startsWith("image/")) {
                return ResponseEntity.badRequest().body("El archivo debe ser una imagen");
            }
            service.subirFoto(id, foto.getBytes());
            return ResponseEntity.ok().body("Foto subida exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la imagen: " + e.getMessage());
        }
    }

    @Operation(summary = "Obtener foto de una mascota", description = "Obtiene la imagen de una mascota específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Foto obtenida exitosamente",
                    content = @Content(mediaType = "image/jpeg")),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada o sin foto")
    })
    @GetMapping("/{id}/foto")
    public ResponseEntity<byte[]> obtenerFoto(
            @Parameter(description = "ID de la mascota", example = "1", required = true)
            @PathVariable Long id) {
        try {
            byte[] foto = service.obtenerFoto(id);
            if (foto == null || foto.length == 0) {
                return ResponseEntity.notFound().build();
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(foto.length);
            // Agregar headers para CORS y cache
            headers.set("Access-Control-Allow-Origin", "*");
            headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            headers.set("Access-Control-Allow-Headers", "*");
            headers.setCacheControl("public, max-age=3600"); // Cache por 1 hora
            headers.set("Content-Disposition", "inline; filename=\"pet_" + id + ".jpg\"");
            return ResponseEntity.ok().headers(headers).body(foto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}