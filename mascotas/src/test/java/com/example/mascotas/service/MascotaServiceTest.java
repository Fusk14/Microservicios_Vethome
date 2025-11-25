package com.example.mascotas.service;

import com.example.mascotas.model.Mascota;
import com.example.mascotas.repository.MascotaRepository;
import com.example.mascotas.webclient.UsuarioClient;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MascotaServiceTest {

    @Mock
    private MascotaRepository repository;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private MascotaService service;

    Mascota getMascotaEjemplo() {
        return new Mascota(1L, 2L, "Fido", "Perro", "Labrador", 4);
    }

    @Test
    void guardar_mascota_conClienteValido() {
        Mascota mascota = getMascotaEjemplo();
        when(usuarioClient.obtenerRolPorId(2L)).thenReturn("CLIENTE");
        when(repository.save(mascota)).thenReturn(mascota);

        Mascota resultado = service.guardar(mascota);

        assertNotNull(resultado);
        verify(repository, times(1)).save(mascota);
    }

    @Test
    void guardar_mascota_conUsuarioNoCliente_lanzaExcepcion() {
        Mascota mascota = getMascotaEjemplo();
        when(usuarioClient.obtenerRolPorId(2L)).thenReturn("VETERINARIO");

        Exception ex = assertThrows(IllegalArgumentException.class, () -> service.guardar(mascota));
        assertEquals("Solo los usuarios con rol CLIENTE pueden registrar mascotas.", ex.getMessage());
    }

    @Test
    void listarMascotas() {
        when(repository.findAll()).thenReturn(List.of(getMascotaEjemplo()));

        List<Mascota> lista = service.listar();

        assertFalse(lista.isEmpty());
        verify(repository).findAll();
    }

    @Test
    void obtenerMascota_existente() {
        Mascota mascota = getMascotaEjemplo();
        when(repository.findById(1L)).thenReturn(Optional.of(mascota));

        Mascota encontrada = service.obtener(1L);
        assertNotNull(encontrada);
        assertEquals("Fido", encontrada.getNombre());
    }

    @Test
    void obtenerMascota_noExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Mascota encontrada = service.obtener(99L);
        assertNull(encontrada);
    }

    @Test
    void eliminarMascota() {
        service.eliminar(1L);
        verify(repository).deleteById(1L);
    }
}
