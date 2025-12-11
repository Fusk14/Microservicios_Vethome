package com.example.usuarios.controller;

import com.example.usuarios.model.Usuario;
import com.example.usuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
@Tag(name = "Usuarios", description = "Operaciones de gestión para usuarios del sistema")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Operation(summary = "Crear un nuevo usuario", description = "Registra un usuario en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
                    content = @Content(schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "400", description = "Datos de usuario inválidos (ej. validaciones fallidas)")
    })
    @PostMapping
    public ResponseEntity<Usuario> crear(
            @Parameter(description = "Objeto Usuario a crear. Contraseña será encriptada.", required = true)
            @Valid @RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuario));
    }

    @Operation(summary = "Listar todos los usuarios", description = "Obtiene una lista de todos los usuarios registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Usuario.class))))
    @GetMapping
    public List<Usuario> listar() {
        return service.listar();
    }

    @Operation(summary = "Obtener un usuario por su ID", description = "Busca un usuario por su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                    content = @Content(schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtener(
            @Parameter(description = "ID del usuario a obtener", example = "1", required = true)
            @PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar usuario por RUT", description = "Busca un usuario por su número de RUT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                    content = @Content(schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/rut/{rut}")
    public ResponseEntity<Usuario> buscarPorRut(
            @Parameter(description = "RUT del usuario a buscar", example = "12345678-9", required = true)
            @PathVariable String rut) {
        return service.buscarPorRut(rut)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar usuario por correo", description = "Busca un usuario mediante su correo electrónico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                    content = @Content(schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/correo/{correo}")
    public ResponseEntity<Usuario> buscarPorCorreo(
            @Parameter(description = "Correo del usuario a buscar", example = "usuario@correo.cl", required = true)
            @PathVariable String correo) {
        return service.buscarPorCorreo(correo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar información del usuario", description = "Actualiza el nombre, apellido y teléfono de un usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Información actualizada exitosamente",
                    content = @Content(schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (nombre o teléfono requeridos)"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarInformacion(
            @Parameter(description = "ID del usuario a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "Datos a actualizar", required = true)
            @RequestBody java.util.Map<String, String> datos) {
        try {
            String nombre = datos.get("nombre");
            String apellido = datos.get("apellido");
            String telefono = datos.get("telefono");

            if (nombre == null || nombre.isBlank()) {
                return ResponseEntity.badRequest().body("El nombre es requerido");
            }
            if (telefono == null || telefono.isBlank()) {
                return ResponseEntity.badRequest().body("El teléfono es requerido");
            }

            Usuario actualizado = service.actualizarInformacion(id, nombre, apellido, telefono);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar la información: " + e.getMessage());
        }
    }

    @Operation(summary = "Cambiar contraseña del usuario", description = "Cambia la contraseña de un usuario verificando la contraseña actual.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contraseña actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Contraseña actual incorrecta, datos inválidos o campos requeridos faltantes"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}/contrasena")
    public ResponseEntity<?> cambiarContrasena(
            @Parameter(description = "ID del usuario", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "Contraseña actual y nueva contraseña", required = true)
            @RequestBody java.util.Map<String, String> datos) {
        try {
            String contrasenaActual = datos.get("contrasenaActual");
            String nuevaContrasena = datos.get("nuevaContrasena");

            if (contrasenaActual == null || contrasenaActual.isBlank()) {
                return ResponseEntity.badRequest().body("La contraseña actual es requerida");
            }
            if (nuevaContrasena == null || nuevaContrasena.isBlank()) {
                return ResponseEntity.badRequest().body("La nueva contraseña es requerida");
            }

            service.cambiarContrasena(id, contrasenaActual, nuevaContrasena);
            return ResponseEntity.ok().body("Contraseña actualizada exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al cambiar la contraseña: " + e.getMessage());
        }
    }
}