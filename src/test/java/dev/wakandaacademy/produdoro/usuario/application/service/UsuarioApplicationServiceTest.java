package dev.wakandaacademy.produdoro.usuario.application.service;

<<<<<<< HEAD
import dev.wakandaacademy.produdoro.DataHelper;
import dev.wakandaacademy.produdoro.usuario.application.repository.UsuarioRepository;
import dev.wakandaacademy.produdoro.usuario.domain.StatusUsuario;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import dev.wakandaacademy.produdoro.usuario.domain.Usuario;
import org.mockito.junit.MockitoJUnitRunner;

=======
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
>>>>>>> dev

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class UsuarioApplicationServiceTest {
    @InjectMocks
    private UsuarioApplicationService usuarioApplicationService;
    @Mock
    private UsuarioRepository usuarioRepository;

    private  static DataHelper dataHelper;

    @Test
    public void usuarioMudaStatusParaFocoTest(){

        Usuario usuario = dataHelper.createUsuario();
        UUID idUsuario = UUID.fromString("a713162f-20a9-4db9-a85b-90cd51ab18f4");

        when(usuarioRepository.buscaUsuarioPorEmail(anyString())).thenReturn(usuario);
        usuarioApplicationService.alteraStatusParaFoco(String.valueOf(usuario), idUsuario);

        verify(usuarioRepository, times(1)).salva(usuario);
    }

    @Test(expected = Exception.class)
    public void usuarioJaEstaEmFocoTest(){
        Usuario usuarioTestRequest = dataHelper.createUsuarioInvalido();

        doThrow(Exception.class)
                .when(usuarioRepository.salva(usuarioTestRequest));
        usuarioApplicationService.alteraStatusParaFoco(usuarioTestRequest.getEmail(), usuarioTestRequest.getIdUsuario());
        
        verify(usuarioRepository, times(1)).salva(usuarioTestRequest);

    }

    @Test(expected = Exception.class)
    public void usuarioPassaTokenInvalidoParaMudarFocoTest(){
        Usuario usuarioTestRequest = dataHelper.createUsuario();
        Usuario usuarioTokenInvalido = dataHelper.createUsuarioInvalido();

        doThrow(Exception.class)
                .when(usuarioRepository.salva(usuarioTestRequest));
        usuarioApplicationService.alteraStatusParaFoco(usuarioTestRequest.getEmail(), usuarioTokenInvalido.getIdUsuario());
        verify(usuarioRepository, times(1));
    }

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