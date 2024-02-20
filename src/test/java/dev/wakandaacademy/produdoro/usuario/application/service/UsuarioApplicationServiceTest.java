package dev.wakandaacademy.produdoro.usuario.application.service;

import static org.mockito.ArgumentMatchers.any;

import dev.wakandaacademy.produdoro.DataHelper;
import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.usuario.application.repository.UsuarioRepository;
import dev.wakandaacademy.produdoro.usuario.domain.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioApplicationServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;
    @InjectMocks
    private UsuarioApplicationService usuarioApplicationService;

    @Test
    void deveAlterarStatusParaPausaLonga() {
        Usuario usuario = DataHelper.createUsuarioFoco();
        when(usuarioRepository.salva(any())).thenReturn(usuario);
        when(usuarioRepository.buscaUsuarioPorEmail(any())).thenReturn(usuario);
        usuarioApplicationService.alteraStatusParaPausaLonga(usuario.getEmail(), usuario.getIdUsuario());
        verify(usuarioRepository, times(1)).salva(any());
    }

    @Test
    void statusParaPausaLongaFalha() {
        Usuario usuario = DataHelper.createUsuario();
        when(usuarioRepository.buscaUsuarioPorEmail(any())).thenReturn(usuario);
        APIException exception = assertThrows(APIException.class, () ->
                usuarioApplicationService.alteraStatusParaPausaLonga("mathias@gmail.com", UUID.randomUUID()));
        assertEquals("credencial de autenticação não e valida", exception.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusException());
    }

    @Test
    void statusJaEstaEmPausaLonga() {
        Usuario usuario = DataHelper.createUsuarioFoco();
        when(usuarioRepository.buscaUsuarioPorEmail(any())).thenReturn(usuario);
        usuarioApplicationService.alteraStatusParaPausaLonga(usuario.getEmail(), usuario.getIdUsuario());
        APIException exception = assertThrows(APIException.class,
                () -> usuarioApplicationService.alteraStatusParaPausaLonga(usuario.getEmail(), usuario.getIdUsuario()));
        assertEquals("Usuário já está em Pausa Longa.", exception.getMessage());
        assertEquals(HttpStatus.CONFLICT, exception.getStatusException());
    }
}