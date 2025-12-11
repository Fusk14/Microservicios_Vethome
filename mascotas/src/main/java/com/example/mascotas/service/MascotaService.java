package com.example.mascotas.service;

import com.example.mascotas.model.Mascota;
import com.example.mascotas.repository.MascotaRepository;
import com.example.mascotas.webclient.UsuarioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MascotaService {

    @Autowired
    private MascotaRepository repository;

    @Autowired
    private UsuarioClient usuarioClient;

    public Mascota guardar(Mascota mascota) {
        String rol = usuarioClient.obtenerRolPorId(mascota.getIdCliente());
        if (!"CLIENTE".equalsIgnoreCase(rol)) {
            throw new IllegalArgumentException("Solo los usuarios con rol CLIENTE pueden registrar mascotas.");
        }
        return repository.save(mascota);
    }

    public List<Mascota> listar() {
        return repository.findAll();
    }

    public Mascota obtener(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    public void subirFoto(Long id, byte[] foto) {
        Mascota mascota = obtener(id);
        if (mascota == null) {
            throw new IllegalArgumentException("Mascota no encontrada");
        }
        mascota.setFoto(foto);
        repository.save(mascota);
    }

    public byte[] obtenerFoto(Long id) {
        Mascota mascota = obtener(id);
        if (mascota == null) {
            throw new IllegalArgumentException("Mascota no encontrada");
        }
        return mascota.getFoto();
    }
}
