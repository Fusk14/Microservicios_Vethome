package com.example.usuarios.controller;

import com.example.usuarios.dto.LoginRequest;
import com.example.usuarios.dto.RegisterRequest;
import com.example.usuarios.model.Rol;
import com.example.usuarios.model.Usuario;
import com.example.usuarios.repository.RolRepository;
import com.example.usuarios.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository; // ✅ Inyectado correctamente

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "Iniciar sesión en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso",
                    content = @Content(schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return usuarioRepository.findByCorreo(request.getCorreo())
                .map(usuario -> {
                    boolean passwordOk = passwordEncoder.matches(request.getContrasena(), usuario.getContrasena());
                    if (!passwordOk) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
                    }
                    // Devolver el usuario completo para que el frontend pueda usarlo directamente
                    // No incluir la contraseña en la respuesta
                    usuario.setContrasena(null);
                    return ResponseEntity.ok(usuario);
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas"));
    }

    @Operation(summary = "Registrar un nuevo usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente",
                    content = @Content(schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (RUT o correo ya registrado, rol no encontrado)"),
            @ApiResponse(responseCode = "409", description = "Conflicto: RUT o correo ya existe en el sistema")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (usuarioRepository.findByRut(req.getRut()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El RUT ya está registrado");
        }

        if (usuarioRepository.findByCorreo(req.getCorreo()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya está registrado");
        }

        // Buscar rol por nombre
        Rol rol = rolRepository.findByNombre(req.getRolNombre())
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + req.getRolNombre()));

        Usuario usuario = new Usuario(
                null,
                req.getRut(),
                req.getNombre(),
                req.getApellido(),
                req.getCorreo(),
                req.getTelefono(),
                passwordEncoder.encode(req.getContrasena()),
                rol
        );

        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @Operation(summary = "Recuperar contraseña", description = "Envía instrucciones para recuperar la contraseña al correo del usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Instrucciones enviadas exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody java.util.Map<String, String> request) {
        String correo = request.get("correo");
        if (correo == null || correo.isBlank()) {
            return ResponseEntity.badRequest().body("El correo es requerido");
        }

        return usuarioRepository.findByCorreo(correo)
                .map(usuario -> {
                    // En una implementación real, aquí se enviaría un email con un token de recuperación
                    // Por ahora, solo retornamos un mensaje de éxito
                    return ResponseEntity.ok().body("Se han enviado las instrucciones para recuperar tu contraseña a: " + correo);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró un usuario con ese correo electrónico"));
    }
}
