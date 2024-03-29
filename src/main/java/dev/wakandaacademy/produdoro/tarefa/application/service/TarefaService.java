package dev.wakandaacademy.produdoro.tarefa.application.service;

import java.util.List;
import java.util.UUID;

import dev.wakandaacademy.produdoro.tarefa.application.api.AlteraTarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaDetalhadoResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaIdResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;

public interface TarefaService {
	TarefaIdResponse criaNovaTarefa(TarefaRequest tarefaRequest);

	Tarefa detalhaTarefa(String usuario, UUID idTarefa);

	void ativaTarefa(String usuario, UUID idTarefa);

	void incrementaPomodoro(String usuario, UUID idTarefa);

	void deletaTarefa(String usuario, UUID idTarefa);

	List<TarefaDetalhadoResponse> listaTodasTarefasDoUsuario(String emailUsuario, UUID idUsuario);

	void alteraTarefa(String emailUsuario, UUID idTarefa, AlteraTarefaRequest alteraTarefaRequest);
}
