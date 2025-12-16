package com.example.usuarios.config;

import com.example.usuarios.model.Rol;
import com.example.usuarios.model.Usuario;
import com.example.usuarios.repository.RolRepository;
import com.example.usuarios.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initData(RolRepository rolRepo, UsuarioRepository userRepo) {
        return args -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            // Crear roles si no existen
            if (rolRepo.count() == 0) {
                rolRepo.save(new Rol(null, "ADMINISTRATIVO"));
                rolRepo.save(new Rol(null, "VETERINARIO"));
                rolRepo.save(new Rol(null, "CLIENTE"));
            }

            if (userRepo.count() == 0) {
                Rol adminRol = rolRepo.findByNombre("ADMINISTRATIVO").orElseThrow();
                Rol vetRol = rolRepo.findByNombre("VETERINARIO").orElseThrow();
                Rol clienteRol = rolRepo.findByNombre("CLIENTE").orElseThrow();

                // Usuario admin con correo válido para el frontend (@duoc.cl)
                userRepo.save(new Usuario(
                        null,
                        "11111111-1",
                        "Admin",
                        "Principal",
                        "admin@duoc.cl",
                        "+56911111111",
                        encoder.encode("admin123"),
                        adminRol
                ));

                userRepo.save(new Usuario(
                        null,
                        "22222222-2",
                        "Carlos",
                        "Cliente",
                        "cliente@correo.cl",
                        "+56922222222",
                        encoder.encode("cliente123"),
                        clienteRol
                ));

                userRepo.save(new Usuario(
                        null,
                        "33333333-3",
                        "Valeria",
                        "Veterinaria",
                        "veterinaria@correo.cl",
                        "+56933333333",
                        encoder.encode("vet123"),
                        vetRol
                ));
            }
        };
    }
}
