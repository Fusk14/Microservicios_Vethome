package com.example.usuarios.service;

import com.example.usuarios.model.Usuario;
import com.example.usuarios.repository.RolRepository;
import com.example.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService service;

    @Mock
    private UsuarioRepository usuarioRepo;

    @Mock
    private RolRepository rolRepo;

    @Mock
    private PasswordEncoder encoder;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void guardarUsuario_codificaContrasena() {
        Usuario u = new Usuario();
        u.setContrasena("clavesecreta");

        when(encoder.encode("clavesecreta")).thenReturn("encriptada");
        when(usuarioRepo.save(any())).thenAnswer(inv -> inv.getArguments()[0]);

        Usuario guardado = service.guardar(u);
        assertEquals("encriptada", guardado.getContrasena());
    }

    
}
