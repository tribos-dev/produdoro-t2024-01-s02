package dev.wakandaacademy.produdoro.tarefa.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import dev.wakandaacademy.produdoro.DataHelper;
import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.tarefa.application.api.AlteraTarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaDetalhadoResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaIdResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.application.repository.TarefaRepository;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;
import dev.wakandaacademy.produdoro.usuario.application.repository.UsuarioRepository;
import dev.wakandaacademy.produdoro.usuario.domain.Usuario;

@ExtendWith(MockitoExtension.class)
class TarefaApplicationServiceTest {

	// @Autowired
	@InjectMocks
	TarefaApplicationService tarefaApplicationService;

	// @MockBean
	@Mock
	TarefaRepository tarefaRepository;

	@Mock
	UsuarioRepository usuarioRepository;

	@Test
	void deveRetornarIdTarefaNovaCriada() {
		TarefaRequest request = getTarefaRequest();
		when(tarefaRepository.salva(any())).thenReturn(new Tarefa(request));

		TarefaIdResponse response = tarefaApplicationService.criaNovaTarefa(request);

		assertNotNull(response);
		assertEquals(TarefaIdResponse.class, response.getClass());
		assertEquals(UUID.class, response.getIdTarefa().getClass());
	}

	public TarefaRequest getTarefaRequest() {
		TarefaRequest request = new TarefaRequest("tarefa 1", UUID.randomUUID(), null, null, 0);
		return request;
	}

	@Test
	void deveRetornarTodasTarefasDoUsuario() {
		String emailUsuario = DataHelper.createUsuario().getEmail();
		UUID idUsuario = DataHelper.createUsuario().getIdUsuario();
		Usuario usuario = DataHelper.createUsuario();
		List<Tarefa> tarefas = DataHelper.createListTarefa();

		when(usuarioRepository.buscaUsuarioPorEmail(any())).thenReturn(usuario);
		when(usuarioRepository.buscaUsuarioPorId(any(UUID.class))).thenReturn(usuario);
		when(tarefaRepository.listaTodasTarefasDoUsuario(any())).thenReturn(tarefas);
		List<TarefaDetalhadoResponse> tarefasDoUsuario = tarefaApplicationService
				.listaTodasTarefasDoUsuario(emailUsuario, idUsuario);

		assertEquals(8, tarefasDoUsuario.size());
		verify(tarefaRepository, times(1)).listaTodasTarefasDoUsuario(idUsuario);
	}

	@Test
	void deveRetornarExceptionQuandoIdForInvalido() {
		Usuario usuarioEmail = DataHelper.createUsuario();
		Usuario usuario = Usuario.builder().idUsuario(UUID.randomUUID()).build();

		when(usuarioRepository.buscaUsuarioPorEmail(any())).thenReturn(usuarioEmail);
		when(usuarioRepository.buscaUsuarioPorId(any(UUID.class))).thenReturn(usuario);

		APIException ex = Assertions.assertThrows(APIException.class, () -> {
			tarefaApplicationService.listaTodasTarefasDoUsuario(usuarioEmail.getEmail(), usuario.getIdUsuario());
		});
		assertEquals("credencial de autenticação não e valida", ex.getMessage());
		assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusException());
	}

	@Test
	void deveAlterarUmaTarefaDoUsuario() {
		Usuario usuario = DataHelper.createUsuario();
		Tarefa tarefa = DataHelper.createTarefa();
		AlteraTarefaRequest request = DataHelper.createAlteraTarefaRequest();
		when(usuarioRepository.buscaUsuarioPorEmail(any())).thenReturn(usuario);
		when(tarefaRepository.buscaTarefaPorId(any())).thenReturn(Optional.of(tarefa));
		tarefaApplicationService.alteraTarefa(usuario.getEmail(), tarefa.getIdTarefa(), request);
		assertEquals(request.getDescricao(), tarefa.getDescricao());
		verify(tarefaRepository, times(1)).salva(tarefa);
	}

	@Test
	void deveRetornarExceptionQuandoIdTarefaForInvalido() {
		UUID idTarefa = UUID.randomUUID();
		Usuario usuario = DataHelper.createUsuario();
		AlteraTarefaRequest request = DataHelper.createAlteraTarefaRequest();
		when(usuarioRepository.buscaUsuarioPorEmail(any())).thenReturn(usuario);
		when(tarefaRepository.buscaTarefaPorId(idTarefa)).thenReturn(Optional.empty());
		APIException ex = assertThrows(APIException.class, () -> {
			tarefaApplicationService.alteraTarefa(usuario.getEmail(), idTarefa, request);
		});
		assertEquals("Tarefa não encontrada!", ex.getMessage());
		assertEquals(HttpStatus.NOT_FOUND, ex.getStatusException());
	}

}
