package com.example.usuarios.service;

import com.example.usuarios.model.Rol;
import com.example.usuarios.model.Usuario;
import com.example.usuarios.repository.RolRepository;
import com.example.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario guardar(Usuario usuario) {
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorRut(String rut) {
        return usuarioRepository.findByRut(rut);
    }

    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    public Rol crearRol(Rol rol, Long idUsuarioSolicitante) {
        Usuario solicitante = usuarioRepository.findById(idUsuarioSolicitante).orElseThrow();
        if (!"ADMINISTRATIVO".equalsIgnoreCase(solicitante.getRol().getNombre())) {
            throw new IllegalArgumentException("Solo los administradores pueden crear roles.");
        }
        return rolRepository.save(rol);
    }

    public List<Rol> listarRoles(Long idUsuarioSolicitante) {
        Usuario solicitante = usuarioRepository.findById(idUsuarioSolicitante).orElseThrow();
        if (!"ADMINISTRATIVO".equalsIgnoreCase(solicitante.getRol().getNombre())) {
            throw new IllegalArgumentException("Solo los administradores pueden ver los roles.");
        }
        return rolRepository.findAll();
    }

    public Usuario actualizarInformacion(Long id, String nombre, String apellido, String telefono) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        usuario.setNombre(nombre);
        if (apellido != null && !apellido.isBlank()) {
            usuario.setApellido(apellido);
        }
        usuario.setTelefono(telefono);
        
        return usuarioRepository.save(usuario);
    }

    public void cambiarContrasena(Long id, String contrasenaActual, String nuevaContrasena) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        // Verificar que la contraseña actual sea correcta
        if (!passwordEncoder.matches(contrasenaActual, usuario.getContrasena())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }
        
        // Encriptar y guardar la nueva contraseña
        usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));
        usuarioRepository.save(usuario);
    }

    
}

