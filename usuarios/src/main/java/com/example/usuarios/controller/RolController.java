package com.example.usuarios.controller;

import com.example.usuarios.model.Rol;
import com.example.usuarios.service.RolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/roles")
@Tag(name = "Roles", description = "Operaciones de gestión para roles (solo administradores)")
public class RolController {

    @Autowired
    private RolService service;

    @Operation(summary = "Crear un nuevo rol (solo admin)", description = "Permite a un administrador crear un nuevo rol.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol creado exitosamente",
                    content = @Content(schema = @Schema(implementation = Rol.class))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado (usuario no administrador o no encontrado)")
    })
    @PostMapping
    public ResponseEntity<?> crear(
            @Parameter(description = "Objeto Rol a crear.", required = true)
            @RequestBody Rol rol,
            @Parameter(description = "ID del usuario que realiza la solicitud (debe ser ADMINISTRATIVO).", example = "1", required = true)
            @RequestParam Long idUsuario) {
        try {
            return ResponseEntity.ok(service.crearRol(rol, idUsuario));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @Operation(summary = "Listar todos los roles (solo admin)", description = "Obtiene una lista de todos los roles, requiere permisos de administrador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de roles obtenida exitosamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Rol.class)))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado (usuario no administrador o no encontrado)")
    })
    @GetMapping
    public ResponseEntity<?> listar(
            @Parameter(description = "ID del usuario que realiza la solicitud (debe ser ADMINISTRATIVO).", example = "1", required = true)
            @RequestParam Long idUsuario) {
        try {
            return ResponseEntity.ok(service.listarRoles(idUsuario));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}