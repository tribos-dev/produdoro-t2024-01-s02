package dev.wakandaacademy.produdoro.usuario.application.service;

import dev.wakandaacademy.produdoro.DataHelper;
import dev.wakandaacademy.produdoro.usuario.application.repository.UsuarioRepository;
import dev.wakandaacademy.produdoro.usuario.domain.StatusUsuario;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import dev.wakandaacademy.produdoro.usuario.domain.Usuario;
import org.mockito.junit.MockitoJUnitRunner;


import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class UsuarioApplicationServiceTest {
    @InjectMocks
    private UsuarioApplicationService target;
    @Mock
    private UsuarioRepository usuarioRepository;

    private  static DataHelper dataHelper;

    @Test
    public void usuarioMudaStatusParaFocoTest(){

        Usuario usuario = dataHelper.createUsuario();
        UUID idUsuario = UUID.fromString("a713162f-20a9-4db9-a85b-90cd51ab18f4");

        when(usuarioRepository.buscaUsuarioPorEmail(anyString())).thenReturn(usuario);
        target.alteraStatusParaFoco(String.valueOf(usuario), idUsuario);

        verify(usuarioRepository, times(1)).salva(usuario);
    }

    @Test(expected = Exception.class)
    public void usuarioJaEstaEmFocoTest(){
        //Given
        Usuario usuarioTestRequest = dataHelper.createUsuarioInvalido();

        doThrow(Exception.class)
                .when(usuarioRepository.salva(usuarioTestRequest));
        target.alteraStatusParaFoco(usuarioTestRequest.getEmail(), usuarioTestRequest.getIdUsuario());
        verify(usuarioRepository, times(1)).salva(usuarioTestRequest);

    }

    @Test(expected = Exception.class)
    public void usuarioPassaTokenInvalidoParaMudarFocoTest(){
        Usuario usuarioTestRequest = dataHelper.createUsuario();
        Usuario usuarioTokenInvalido = dataHelper.createUsuarioInvalido();

        doThrow(Exception.class)
                .when(usuarioRepository.salva(usuarioTestRequest));
        target.alteraStatusParaFoco(usuarioTestRequest.getEmail(), usuarioTokenInvalido.getIdUsuario());
        verify(usuarioRepository, times(1));
    }
}