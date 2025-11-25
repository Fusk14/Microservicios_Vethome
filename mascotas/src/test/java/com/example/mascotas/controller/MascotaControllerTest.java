package com.example.mascotas.controller;


import com.example.mascotas.model.Mascota;
import com.example.mascotas.service.MascotaService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MascotaController.class)

public class MascotaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MascotaService mascotaService;

    @Autowired
    private ObjectMapper objectMapper;

    Mascota getMascotaEjemplo() {
        return new Mascota(1L, 2L, "Fido", "Perro", "Labrador", 4);
    }

    @Test
    void crearMascota_valida_retorna200() throws Exception {
        Mascota mascota = getMascotaEjemplo();
        Mockito.when(mascotaService.guardar(any(Mascota.class))).thenReturn(mascota);

        mockMvc.perform(post("/api/mascotas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mascota)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Fido"));
    }

    @Test
    void crearMascota_invalida_retorna400() throws Exception {
        Mascota mascotaInvalida = new Mascota(null, null, "", null, null, 0); // nombre en blanco y cliente null

        mockMvc.perform(post("/api/mascotas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mascotaInvalida)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listarMascotas_retornaLista() throws Exception {
        Mockito.when(mascotaService.listar()).thenReturn(List.of(getMascotaEjemplo()));

        mockMvc.perform(get("/api/mascotas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Fido"));
    }

    @Test
    void obtenerMascota_existente_retorna200() throws Exception {
        Mockito.when(mascotaService.obtener(1L)).thenReturn(getMascotaEjemplo());

        mockMvc.perform(get("/api/mascotas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.especie").value("Perro"));
    }

    @Test
    void obtenerMascota_noExiste_retorna404() throws Exception {
        Mockito.when(mascotaService.obtener(99L)).thenReturn(null);

        mockMvc.perform(get("/api/mascotas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarMascota() throws Exception {
        mockMvc.perform(delete("/api/mascotas/1"))
                .andExpect(status().isNoContent());
    }
}
