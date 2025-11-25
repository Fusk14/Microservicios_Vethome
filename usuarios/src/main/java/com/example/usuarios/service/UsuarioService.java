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

    
}

