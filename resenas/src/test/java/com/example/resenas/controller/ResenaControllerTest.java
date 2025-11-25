package com.example.resenas.controller;

import com.example.resenas.model.Resena;
import com.example.resenas.service.ResenaService;
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

@WebMvcTest(ResenaController.class)
public class ResenaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResenaService service;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void crear_resenaValida_retorna200() throws Exception {
        Resena r = new Resena(null, 2L, 3L, 5, "Comentario");
        Mockito.when(service.guardar(any(Resena.class))).thenReturn(r);

        mockMvc.perform(post("/api/resenas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(r)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comentario").value("Comentario"));
    }

    

    @Test
    void listar_devuelveLista() throws Exception {
        Mockito.when(service.listar()).thenReturn(List.of(new Resena(1L, 2L, 3L, 5, "Todo ok")));

        mockMvc.perform(get("/api/resenas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].comentario").value("Todo ok"));
    }
}
