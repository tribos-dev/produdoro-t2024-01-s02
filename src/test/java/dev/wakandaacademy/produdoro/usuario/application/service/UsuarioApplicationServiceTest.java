package dev.wakandaacademy.produdoro.usuario.application.service;

import dev.wakandaacademy.produdoro.DataHelper;
import dev.wakandaacademy.produdoro.usuario.application.repository.UsuarioRepository;
import dev.wakandaacademy.produdoro.usuario.domain.StatusUsuario;
import dev.wakandaacademy.produdoro.usuario.domain.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioApplicationServiceTest {
    @InjectMocks
    private UsuarioApplicationService usuarioApplicationService;
    @Mock
    private UsuarioRepository usuarioRepository;

    @Test
    void deveMudarStatusParaPausaCurta() {
        //Given
        Usuario usuario = DataHelper.createUsuario();
        //When
        when(usuarioRepository.buscaUsuarioPorEmail(any())).thenReturn(usuario);
        usuarioApplicationService.mudaStatusParaPausaCurta(usuario.getIdUsuario(), usuario.getEmail());
        //Then
        verify(usuarioRepository, times(1)).buscaUsuarioPorEmail(any());
        verify(usuarioRepository, times(1)).buscaUsuarioPorId(any());
        verify(usuarioRepository, times(1)).salva(any());
        assertEquals(StatusUsuario.PAUSA_CURTA, usuario.getStatus());
    }
}