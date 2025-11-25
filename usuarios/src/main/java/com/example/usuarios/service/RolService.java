package com.example.usuarios.service;

import com.example.usuarios.model.Rol;
import com.example.usuarios.model.Usuario;
import com.example.usuarios.repository.RolRepository;
import com.example.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Rol crearRol(Rol rol, Long idUsuarioSolicitante) {
        Usuario admin = usuarioRepository.findById(idUsuarioSolicitante)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!"ADMINISTRATIVO".equalsIgnoreCase(admin.getRol().getNombre())) {
            throw new IllegalArgumentException("Solo los administradores pueden crear roles.");
        }

        return rolRepository.save(rol);
    }

    public List<Rol> listarRoles(Long idUsuarioSolicitante) {
        Usuario admin = usuarioRepository.findById(idUsuarioSolicitante)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!"ADMINISTRATIVO".equalsIgnoreCase(admin.getRol().getNombre())) {
            throw new IllegalArgumentException("Solo los administradores pueden ver los roles.");
        }

        return rolRepository.findAll();
    }
}