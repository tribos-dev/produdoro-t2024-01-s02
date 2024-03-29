package dev.wakandaacademy.produdoro.tarefa.application.api;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import dev.wakandaacademy.produdoro.config.security.service.TokenService;
import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.tarefa.application.service.TarefaService;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequiredArgsConstructor
public class TarefaRestController implements TarefaAPI {
	private final TarefaService tarefaService;
	private final TokenService tokenService;

	public TarefaIdResponse postNovaTarefa(TarefaRequest tarefaRequest) {
		log.info("[inicia]  TarefaRestController - postNovaTarefa  ");
		TarefaIdResponse tarefaCriada = tarefaService.criaNovaTarefa(tarefaRequest);
		log.info("[finaliza]  TarefaRestController - postNovaTarefa");
		return tarefaCriada;
	}

	@Override
	public TarefaDetalhadoResponse detalhaTarefa(String token, UUID idTarefa) {
		log.info("[inicia] TarefaRestController - detalhaTarefa");
		String usuario = getUsuarioByToken(token);
		Tarefa tarefa = tarefaService.detalhaTarefa(usuario, idTarefa);
		log.info("[finaliza] TarefaRestController - detalhaTarefa");
		return new TarefaDetalhadoResponse(tarefa);
	}

	@Override
	public ResponseEntity<String> ativaTarefa(String token, UUID idTarefa) {
		log.info("[inicia] TarefaRestController - ativaTarefa");
		String usuario = getUsuarioByToken(token);
		tarefaService.ativaTarefa(usuario, idTarefa);
		log.info("[finaliza] TarefaRestController - ativaTarefa");
		return ResponseEntity.ok("Tarefa Ativada com Sucesso!");
	}

	@Override
	public void incrementaPomodoro(String token, UUID idTarefa) {
		log.info("[inicia] TarefaRestController - incrementaPomodoro");
		String usuario = getUsuarioByToken(token);
		tarefaService.incrementaPomodoro(usuario, idTarefa);
		log.info("[finaliza] TarefaRestController - incrementaPomodoro");
	}

	@Override
	public void deleteTarefa(String token, UUID idTarefa) {
		log.info("[inicia] TarefaRestController - deleteTarefa");
		String usuario = getUsuarioByToken(token);
		tarefaService.deletaTarefa(usuario, idTarefa);
		log.info("[finaliza] TarefaRestController - deleteTarefa");
	}

	@Override
	public List<TarefaDetalhadoResponse> listaTodasTarefasDoUsuario(String token, UUID idUsuario) {
		log.info("[inicia] TarefaRestController - listaTodasTarefasDoUsuario");
		String emailUsuario = getUsuarioByToken(token);
		List<TarefaDetalhadoResponse> listaTarefas = tarefaService.listaTodasTarefasDoUsuario(emailUsuario, idUsuario);
		log.info("[finaliza] TarefaRestController - listaTodasTarefasDoUsuario");
		return listaTarefas;
	}

	@Override
	public void alteraTarefa(String token, UUID idTarefa, AlteraTarefaRequest alteraTarefaRequest) {
		log.info("[inicia] TarefaRestController - alteraTarefa");
		String emailUsuario = getUsuarioByToken(token);
		tarefaService.alteraTarefa(emailUsuario, idTarefa, alteraTarefaRequest);
		log.info("[finaliza] TarefaRestController - alteraTarefa");
	}

	private String getUsuarioByToken(String token) {
		log.debug("[token] {}", token);
		String usuario = tokenService.getUsuarioByBearerToken(token)
				.orElseThrow(() -> APIException.build(HttpStatus.UNAUTHORIZED, token));
		log.info("[usuario] {}", usuario);
		return usuario;
	}
}
