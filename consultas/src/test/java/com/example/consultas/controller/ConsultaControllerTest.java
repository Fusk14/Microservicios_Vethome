package com.example.consultas.controller;

import com.example.consultas.model.Consulta;
import com.example.consultas.service.ConsultaService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConsultaController.class)
//@Import(SecurityTestConfig.class)
public class ConsultaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConsultaService service;

    @Autowired
    private ObjectMapper mapper;

    private Consulta getConsultaEjemplo() {
        return new Consulta(1L, 1L, 2L, 3L, LocalDate.now(), "Chequeo", null, null);
    }

    @Test
    void crearConsulta_valida_retorna200() throws Exception {
        Consulta consulta = getConsultaEjemplo();
        when(service.guardar(any(Consulta.class))).thenReturn(consulta);

        mockMvc.perform(post("/api/consultas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(consulta)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.motivo").value("Chequeo"));
    }

    @Test
    void listarConsultas_retornaLista() throws Exception {
        when(service.listar()).thenReturn(List.of(getConsultaEjemplo()));

        mockMvc.perform(get("/api/consultas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idMascota").value(1L));
    }

    @Test
    void obtenerConsulta_existente() throws Exception {
        when(service.obtener(1L)).thenReturn(getConsultaEjemplo());

        mockMvc.perform(get("/api/consultas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idVeterinario").value(2L));
    }

    @Test
    void eliminarConsulta() throws Exception {
        mockMvc.perform(delete("/api/consultas/1"))
                .andExpect(status().isNoContent());
    }
}
