package dev.wakandaacademy.produdoro.usuario.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import dev.wakandaacademy.produdoro.DataHelper;
import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.usuario.application.repository.UsuarioRepository;
import dev.wakandaacademy.produdoro.usuario.domain.Usuario;
import dev.wakandaacademy.produdoro.usuario.domain.StatusUsuario;

@ExtendWith(MockitoExtension.class)
public class UsuarioApplicationServiceTest {
	@InjectMocks
	private UsuarioApplicationService usuarioApplicationService;
	@Mock
	private UsuarioRepository usuarioRepository;

	@Test
	public void usuarioMudaStatusParaFocoTest() {

		Usuario usuario = DataHelper.createUsuario();
		UUID idUsuario = UUID.fromString("a713162f-20a9-4db9-a85b-90cd51ab18f4");

		when(usuarioRepository.buscaUsuarioPorEmail(anyString())).thenReturn(usuario);
		when(usuarioRepository.buscaUsuarioPorId(idUsuario)).thenReturn(usuario);
		usuarioApplicationService.alteraStatusParaFoco(usuario.getEmail(), idUsuario);

		verify(usuarioRepository, times(1)).salva(any());
	}

//	@Test
//	public void usuarioJaEstaEmFocoTest() {
//		Usuario usuario = DataHelper.createUsuarioFoco();
//		
//		when(usuarioRepository.buscaUsuarioPorEmail(any())).thenReturn(usuario);
//		when(usuarioRepository.buscaUsuarioPorId(usuario.getIdUsuario())).thenReturn(usuario);
//
//		usuarioApplicationService.alteraStatusParaFoco(usuario.getEmail(), usuario.getIdUsuario());
//		APIException exception = assertThrows(APIException.class,
//				() -> usuarioApplicationService.alteraStatusParaPausaLonga(usuario.getEmail(), usuario.getIdUsuario()));
//		assertEquals("Status do usuário já está em foco", exception.getMessage());
//		assertEquals(HttpStatus.CONFLICT, exception.getStatusException());
//	}
	

	@Test
	public void usuarioPassaTokenInvalidoParaMudarFocoTest() {
		Usuario usuarioTestRequest = DataHelper.createUsuario();

		when(usuarioRepository.buscaUsuarioPorEmail(any())).thenReturn(usuarioTestRequest);
		APIException exception = assertThrows(APIException.class,
				() -> usuarioApplicationService
				.alteraStatusParaFoco("catharina@gmail.com", UUID.randomUUID()));
		assertEquals("Credencial de autenticação não é válida", exception.getMessage());
		assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusException());
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
		APIException exception = assertThrows(APIException.class,
				() -> usuarioApplicationService.alteraStatusParaPausaLonga("mathias@gmail.com", UUID.randomUUID()));
		assertEquals("Credencial de autenticação não é válida", exception.getMessage());
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