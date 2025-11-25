package com.example.resenas.service;


import com.example.resenas.model.Resena;
import com.example.resenas.repository.ResenaRepository;
import com.example.resenas.webclient.UsuarioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResenaService {

    @Autowired
    private ResenaRepository repo;

    @Autowired
    private UsuarioClient usuarioClient;

    public Resena guardar(Resena resena) {
        String rolCliente = usuarioClient.obtenerRol(resena.getIdCliente());
        String rolVet = usuarioClient.obtenerRol(resena.getIdVeterinario());

        if (!"CLIENTE".equalsIgnoreCase(rolCliente)) {
            throw new IllegalArgumentException("Solo los usuarios con rol CLIENTE pueden escribir reseñas.");
        }

        if (!"VETERINARIO".equalsIgnoreCase(rolVet)) {
            throw new IllegalArgumentException("Las reseñas solo pueden dirigirse a usuarios con rol VETERINARIO.");
        }

        return repo.save(resena);
    }

    public List<Resena> listar() {
        return repo.findAll();
    }

    public Resena obtener(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
