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
        System.out.println("📝 Guardando reseña - Cliente ID: " + resena.getIdCliente() + ", Veterinario ID: " + resena.getIdVeterinario());
        
        String rolCliente = usuarioClient.obtenerRol(resena.getIdCliente());
        String rolVet = usuarioClient.obtenerRol(resena.getIdVeterinario());

        System.out.println("   - Rol Cliente: " + (rolCliente != null ? rolCliente : "null"));
        System.out.println("   - Rol Veterinario: " + (rolVet != null ? rolVet : "null"));

        if (rolCliente == null) {
            System.out.println("❌ No se pudo obtener el rol del cliente ID: " + resena.getIdCliente());
            throw new IllegalArgumentException("No se pudo verificar el rol del cliente. Por favor, intenta nuevamente.");
        }

        if (rolVet == null) {
            System.out.println("❌ No se pudo obtener el rol del veterinario ID: " + resena.getIdVeterinario());
            throw new IllegalArgumentException("No se pudo verificar el rol del veterinario. Por favor, intenta nuevamente.");
        }

        if (!"CLIENTE".equalsIgnoreCase(rolCliente)) {
            System.out.println("❌ El usuario ID " + resena.getIdCliente() + " no tiene rol CLIENTE, tiene: " + rolCliente);
            throw new IllegalArgumentException("Solo los usuarios con rol CLIENTE pueden escribir reseñas.");
        }

        if (!"VETERINARIO".equalsIgnoreCase(rolVet)) {
            System.out.println("❌ El usuario ID " + resena.getIdVeterinario() + " no tiene rol VETERINARIO, tiene: " + rolVet);
            throw new IllegalArgumentException("Las reseñas solo pueden dirigirse a usuarios con rol VETERINARIO.");
        }

        System.out.println("✅ Validaciones pasadas, guardando reseña en base de datos...");
        Resena guardada = repo.save(resena);
        System.out.println("✅ Reseña guardada exitosamente con ID: " + guardada.getId());
        return guardada;
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
