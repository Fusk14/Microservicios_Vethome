package com.example.usuarios.controller;

import com.example.usuarios.dto.LoginRequest;
import com.example.usuarios.dto.RegisterRequest;
import com.example.usuarios.model.Rol;
import com.example.usuarios.model.Usuario;
import com.example.usuarios.repository.RolRepository;
import com.example.usuarios.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
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
            @ApiResponse(responseCode = "200", description = "Login exitoso"),
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
                    return ResponseEntity.ok("Login exitoso para el usuario: " + usuario.getCorreo());
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas"));
    }

    @Operation(summary = "Registrar un nuevo usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (usuarioRepository.findByRut(req.getRut()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El RUT ya está registrado");
        }

        if (usuarioRepository.findByCorreo(req.getCorreo()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El correo ya está registrado");
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
        return ResponseEntity.ok(nuevoUsuario);
    }
}
