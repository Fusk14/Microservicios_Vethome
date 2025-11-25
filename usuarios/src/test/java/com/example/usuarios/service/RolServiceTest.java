package com.example.usuarios.service;

import com.example.usuarios.model.Rol;
import com.example.usuarios.model.Usuario;
import com.example.usuarios.repository.RolRepository;
import com.example.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RolServiceTest {

    @InjectMocks
    private RolService rolService;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearRol_conUsuarioAdmin_debeGuardarYRetornarRol() {
        Rol nuevoRol = new Rol(null, "VETERINARIO");
        Usuario admin = new Usuario();
        admin.setRol(new Rol(1L, "ADMINISTRATIVO"));

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(rolRepository.save(nuevoRol)).thenReturn(new Rol(2L, "VETERINARIO"));

        Rol creado = rolService.crearRol(nuevoRol, 1L);
        assertEquals("VETERINARIO", creado.getNombre());
    }

    @Test
    void crearRol_conUsuarioNoAdmin_lanzaExcepcion() {
        Usuario cliente = new Usuario();
        cliente.setRol(new Rol(2L, "CLIENTE"));

        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(cliente));

        assertThrows(IllegalArgumentException.class, () ->
            rolService.crearRol(new Rol(null, "CLIENTE"), 2L));
    }

    @Test
    void listarRoles_conUsuarioAdmin_retornaLista() {
        Usuario admin = new Usuario();
        admin.setRol(new Rol(1L, "ADMINISTRATIVO"));

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(rolRepository.findAll()).thenReturn(List.of(new Rol(1L, "CLIENTE")));

        List<Rol> roles = rolService.listarRoles(1L);

        assertEquals(1, roles.size());
        assertEquals("CLIENTE", roles.get(0).getNombre());
    }

    @Test
    void listarRoles_conUsuarioNoAdmin_lanzaExcepcion() {
        Usuario vet = new Usuario();
        vet.setRol(new Rol(2L, "VETERINARIO"));

        when(usuarioRepository.findById(3L)).thenReturn(Optional.of(vet));

        assertThrows(IllegalArgumentException.class, () ->
            rolService.listarRoles(3L));
    }
}
