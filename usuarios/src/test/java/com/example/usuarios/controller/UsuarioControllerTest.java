package com.example.usuarios.controller;

import com.example.usuarios.config.SecurityTestConfig;
import com.example.usuarios.model.Rol;
import com.example.usuarios.model.Usuario;
import com.example.usuarios.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityTestConfig.class)
@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService service;

    @Autowired
    private ObjectMapper mapper;

    private Usuario getEjemploUsuario() {
        return new Usuario(1L, "12345678-9", "Luis", "Hurtubia", "luis@example.com", "+56912345678", "clave123", new Rol(1L, "CLIENTE"));
    }

    @Test
    void crearUsuario_retorna200() throws Exception {
        Usuario usuario = getEjemploUsuario();
        when(service.guardar(any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Luis"))
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }

    @Test
    void listarUsuarios_retornaLista() throws Exception {
        when(service.listar()).thenReturn(List.of(getEjemploUsuario()));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].correo").value("luis@example.com"));
    }

    @Test
    void obtenerUsuarioPorId_existente() throws Exception {
        Usuario usuario = getEjemploUsuario();
        when(service.buscarPorId(1L)).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.apellido").value("Hurtubia"));
    }

    @Test
    void obtenerUsuarioPorId_noExistente() throws Exception {
        when(service.buscarPorId(100L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarUsuarioPorRut_existente() throws Exception {
        Usuario usuario = getEjemploUsuario();
        when(service.buscarPorRut("12345678-9")).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/api/usuarios/rut/12345678-9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Luis"));
    }

    @Test
    void buscarUsuarioPorRut_noExistente() throws Exception {
        when(service.buscarPorRut("00000000-0")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/rut/00000000-0"))
                .andExpect(status().isNotFound()); 
    }
}
