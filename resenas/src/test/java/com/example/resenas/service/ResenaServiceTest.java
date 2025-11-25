package com.example.resenas.service;

import com.example.resenas.model.Resena;
import com.example.resenas.repository.ResenaRepository;
import com.example.resenas.webclient.UsuarioClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ResenaServiceTest {

    @InjectMocks
    private ResenaService service;

    @Mock
    private ResenaRepository repo;

    @Mock
    private UsuarioClient usuarioClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void guardar_clienteYVeterinarioValidos_guardaCorrectamente() {
        Resena r = new Resena(null, 2L, 3L, 5, "Muy buena atención");

        when(usuarioClient.obtenerRol(2L)).thenReturn("CLIENTE");
        when(usuarioClient.obtenerRol(3L)).thenReturn("VETERINARIO");
        when(repo.save(r)).thenReturn(r);

        Resena resultado = service.guardar(r);

        assertEquals("Muy buena atención", resultado.getComentario());
        verify(repo, times(1)).save(r);
    }

    @Test
    void guardar_usuarioNoEsCliente_lanzaExcepcion() {
        Resena r = new Resena(null, 2L, 3L, 4, "Comentario");

        when(usuarioClient.obtenerRol(2L)).thenReturn("VETERINARIO");

        Exception ex = assertThrows(IllegalArgumentException.class, () -> service.guardar(r));
        assertEquals("Solo los usuarios con rol CLIENTE pueden escribir reseñas.", ex.getMessage());
    }

    @Test
    void guardar_destinatarioNoEsVeterinario_lanzaExcepcion() {
        Resena r = new Resena(null, 2L, 3L, 4, "Comentario");

        when(usuarioClient.obtenerRol(2L)).thenReturn("CLIENTE");
        when(usuarioClient.obtenerRol(3L)).thenReturn("ADMINISTRATIVO");

        Exception ex = assertThrows(IllegalArgumentException.class, () -> service.guardar(r));
        assertEquals("Las reseñas solo pueden dirigirse a usuarios con rol VETERINARIO.", ex.getMessage());
    }

    @Test
    void listar_devuelveTodasLasResenas() {
        when(repo.findAll()).thenReturn(List.of(new Resena()));

        List<Resena> lista = service.listar();

        assertEquals(1, lista.size());
    }

    @Test
    void obtener_resenaExiste_devuelveResena() {
        Resena r = new Resena(1L, 2L, 3L, 5, "Comentario");
        when(repo.findById(1L)).thenReturn(Optional.of(r));

        Resena resultado = service.obtener(1L);

        assertNotNull(resultado);
        assertEquals(2L, resultado.getIdCliente());
    }

    @Test
    void eliminar_resenaEliminada() {
        service.eliminar(1L);
        verify(repo, times(1)).deleteById(1L);
    }
}
