package com.example.consultas.service;

import com.example.consultas.model.Consulta;
import com.example.consultas.repository.ConsultaRepository;
import com.example.consultas.webclient.NotificacionClient;
import com.example.consultas.webclient.UsuarioClient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ConsultaServiceTest {

    @InjectMocks
    private ConsultaService consultaService;

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private NotificacionClient notificacionClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void guardarConsulta_clienteYvetValidos_debeGuardar() {
        Consulta consulta = new Consulta(null, 1L, 2L, 3L, LocalDate.now(), "control", null, null);

        when(usuarioClient.obtenerRolPorId(3L)).thenReturn("CLIENTE");
        when(usuarioClient.obtenerRolPorId(2L)).thenReturn("VETERINARIO");
        when(consultaRepository.save(any(Consulta.class))).thenReturn(consulta);

        Consulta resultado = consultaService.guardar(consulta);

        assertEquals(3L, resultado.getIdCliente());
        verify(notificacionClient).enviarNotificacion(eq(3L), contains("Su consulta ha sido agendada"));
    }

    @Test
    void guardarConsulta_rolInvalido_lanzaExcepcion() {
        Consulta consulta = new Consulta(null, 1L, 2L, 3L, LocalDate.now(), "control", null, null);

        when(usuarioClient.obtenerRolPorId(3L)).thenReturn("VETERINARIO"); // cliente no válido

        Exception e = assertThrows(IllegalArgumentException.class, () ->
            consultaService.guardar(consulta));

        assertTrue(e.getMessage().contains("CLIENTE"));
    }
}
