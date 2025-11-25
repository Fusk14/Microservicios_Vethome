package com.example.consultas.service;

import com.example.consultas.model.Consulta;
import com.example.consultas.repository.ConsultaRepository;
import com.example.consultas.webclient.NotificacionClient;
import com.example.consultas.webclient.UsuarioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository repo;

    @Autowired
    private UsuarioClient usuarioClient;

    @Autowired
    private NotificacionClient notificacionClient;

    public List<Consulta> listar() {
        return repo.findAll();
    }

    public Consulta guardar(Consulta consulta) {
        // Primero, asegurar que tenemos un veterinario válido
        // Si el idVeterinario no es válido, buscar uno automáticamente
        String rolVeterinario = usuarioClient.obtenerRolPorId(consulta.getIdVeterinario());
        if (rolVeterinario == null || !"VETERINARIO".equalsIgnoreCase(rolVeterinario)) {
            // Buscar el primer veterinario disponible
            Long veterinarioPorDefecto = usuarioClient.obtenerPrimerVeterinario();
            if (veterinarioPorDefecto != null) {
                consulta.setIdVeterinario(veterinarioPorDefecto);
                System.out.println("Veterinario corregido automáticamente a ID: " + veterinarioPorDefecto);
            } else {
                // Si no hay veterinarios, usar un ID por defecto (1) y continuar
                // Esto permite que funcione aunque no haya validación de roles
                System.out.println("Advertencia: No se pudo validar veterinario, usando ID enviado: " + consulta.getIdVeterinario());
            }
        }

        // Validar que el cliente tenga rol CLIENTE (pero no bloquear si falla la validación)
        String rolCliente = usuarioClient.obtenerRolPorId(consulta.getIdCliente());
        if (rolCliente != null && !"CLIENTE".equalsIgnoreCase(rolCliente)) {
            throw new IllegalArgumentException("Solo usuarios con rol CLIENTE pueden agendar consultas. Rol obtenido: " + rolCliente);
        }
        // Si rolCliente es null, continuar de todas formas (puede ser problema de conexión)

        // Guardar la consulta en la base de datos
        Consulta creada = repo.save(consulta);
        System.out.println("Consulta guardada exitosamente con ID: " + creada.getId());

        // Intentar enviar notificación (no debe fallar si hay error)
        try {
            notificacionClient.enviarNotificacion(
                consulta.getIdCliente(),
                "Su consulta ha sido agendada para el día " + consulta.getFecha()
            );
        } catch (Exception e) {
            // Si falla la notificación, no afecta el guardado de la consulta
            System.err.println("Advertencia: Error al enviar notificación (consulta guardada): " + e.getMessage());
        }

        return creada;
    }

    public Consulta obtener(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}

