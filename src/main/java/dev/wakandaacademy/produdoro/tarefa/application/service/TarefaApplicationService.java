package dev.wakandaacademy.produdoro.tarefa.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.tarefa.application.api.AlteraTarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaDetalhadoResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaIdResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.application.repository.TarefaRepository;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;
import dev.wakandaacademy.produdoro.usuario.application.repository.UsuarioRepository;
import dev.wakandaacademy.produdoro.usuario.domain.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class TarefaApplicationService implements TarefaService {
	private final TarefaRepository tarefaRepository;
	private final UsuarioRepository usuarioRepository;

	@Override
	public TarefaIdResponse criaNovaTarefa(TarefaRequest tarefaRequest) {
		log.info("[inicia] TarefaApplicationService - criaNovaTarefa");
		Tarefa tarefaCriada = tarefaRepository.salva(new Tarefa(tarefaRequest));
		log.info("[finaliza] TarefaApplicationService - criaNovaTarefa");
		return TarefaIdResponse.builder().idTarefa(tarefaCriada.getIdTarefa()).build();
	}

	@Override
	public Tarefa detalhaTarefa(String usuario, UUID idTarefa) {
		log.info("[inicia] TarefaApplicationService - detalhaTarefa");
		Usuario usuarioPorEmail = usuarioRepository.buscaUsuarioPorEmail(usuario);
		log.info("[usuarioPorEmail] {}", usuarioPorEmail);
		Tarefa tarefa = tarefaRepository.buscaTarefaPorId(idTarefa)
				.orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Tarefa não encontrada!"));
		tarefa.pertenceAoUsuario(usuarioPorEmail);
		log.info("[finaliza] TarefaApplicationService - detalhaTarefa");
		return tarefa;
	}

	@Override
	public void ativaTarefa(String usuario, UUID idTarefa) {
		log.info("[inicia] TarefaApplicationService - ativaTarefa");
		Tarefa tarefa = detalhaTarefa(usuario, idTarefa);
		tarefaRepository.desativaTarefasAtiva(tarefa.getIdUsuario());
		tarefa.ativaTarefa();
		tarefaRepository.salva(tarefa);
		log.info("[finaliza] TarefaApplicationService - ativaTarefa");
	}

	@Override
	public void incrementaPomodoro(String usuario, UUID idTarefa) {
		log.info("[inicia] TarefaApplicationService - incrementaPomodoro");
		Tarefa tarefa = detalhaTarefa(usuario, idTarefa);
		tarefa.incrementaPomodoro();
		tarefaRepository.salva(tarefa);
		log.info("[finaliza] TarefaApplicationService - incrementaPomodoro");
	}

	public void deletaTarefa(String usuario, UUID idTarefa) {
		log.info("[inicia] TarefaApplicationService - deletaTarefa");
		Tarefa tarefa = detalhaTarefa(usuario, idTarefa);
		tarefaRepository.deletaTarefa(tarefa);
		log.info("[finaliza] TarefaApplicationService - deletaTarefa");
	}

	@Override
	public List<TarefaDetalhadoResponse> listaTodasTarefasDoUsuario(String emailUsuario, UUID idUsuario) {
		log.info("[inicia] TarefaApplicationService - listaTodasTarefasDoUsuario");
		Usuario usuarioToken = usuarioRepository.buscaUsuarioPorEmail(emailUsuario);
		Usuario usuario = usuarioRepository.buscaUsuarioPorId(idUsuario);
		usuario.validaUsuario(usuarioToken.getIdUsuario());
		List<Tarefa> tarefasDoUsuario = tarefaRepository.listaTodasTarefasDoUsuario(usuario.getIdUsuario());
		log.info("[finaliza] TarefaApplicationService - listaTodasTarefasDoUsuario");
		return TarefaDetalhadoResponse.converte(tarefasDoUsuario);
	}

	@Override
	public void alteraTarefa(String emailUsuario, UUID idTarefa, AlteraTarefaRequest alteraTarefaRequest) {
		log.info("[inicia] TarefaApplicationService - alteraTarefa");
		Tarefa tarefa = detalhaTarefa(emailUsuario, idTarefa);
		tarefa.alteraTarefa(alteraTarefaRequest);
		tarefaRepository.salva(tarefa);
		log.info("[finaliza] TarefaApplicationService - alteraTarefa");
	}
}
