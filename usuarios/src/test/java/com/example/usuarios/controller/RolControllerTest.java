package com.example.usuarios.controller;

import com.example.usuarios.config.SecurityTestConfig;
import com.example.usuarios.model.Rol;
import com.example.usuarios.service.RolService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RolController.class)
@Import(SecurityTestConfig.class) // Desactiva seguridad
public class RolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RolService rolService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crearRol_conUsuarioAdmin_retorna200() throws Exception {
        Rol nuevo = new Rol(null, "CLIENTE");
        Rol creado = new Rol(1L, "CLIENTE");

        Mockito.when(rolService.crearRol(any(Rol.class), eq(1L))).thenReturn(creado);

        mockMvc.perform(post("/api/roles")
                .param("idUsuario", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("CLIENTE"));
    }

    @Test
    void crearRol_conUsuarioNoAdmin_retorna403() throws Exception {
        Rol nuevo = new Rol(null, "CLIENTE");

        Mockito.when(rolService.crearRol(any(Rol.class), eq(2L)))
                .thenThrow(new IllegalArgumentException("Solo los administradores pueden crear roles."));

        mockMvc.perform(post("/api/roles")
                .param("idUsuario", "2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Solo los administradores pueden crear roles."));
    }

    @Test
    void listarRoles_conUsuarioAdmin_retornaLista() throws Exception {
        Mockito.when(rolService.listarRoles(1L)).thenReturn(List.of(new Rol(1L, "ADMINISTRATIVO")));

        mockMvc.perform(get("/api/roles")
                .param("idUsuario", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("ADMINISTRATIVO"));
    }
}
