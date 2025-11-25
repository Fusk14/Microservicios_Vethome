package com.example.resenas.service;

import com.example.resenas.model.Notificacion;
import com.example.resenas.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository repo;

    public Notificacion enviar(Notificacion n) {
        return repo.save(n);
    }

    public List<Notificacion> listar() {
        return repo.findAll();
    }

    public Notificacion obtener(Long id) {
        return repo.findById(id).orElse(null);
    }

    public List<Notificacion> listarPorUsuario(Long idUsuario) {
        return repo.findByIdUsuario(idUsuario);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
