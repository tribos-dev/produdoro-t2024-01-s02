package dev.wakandaacademy.produdoro.tarefa.infra;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.tarefa.application.repository.TarefaRepository;
import dev.wakandaacademy.produdoro.tarefa.domain.StatusAtivacaoTarefa;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Repository
@Log4j2
@RequiredArgsConstructor
public class TarefaInfraRepository implements TarefaRepository {

	private static final String ID_USUARIO_FIELD = "idUsuario";
	private static final String STATUS_ATIVACAO_FIELD = "statusAtivacao";

	private final MongoTemplate mongoTemplate;
	private final TarefaSpringMongoDBRepository tarefaSpringMongoDBRepository;

	@Override
	public Tarefa salva(Tarefa tarefa) {
		log.info("[inicia] TarefaInfraRepository - salva");
		try {
			tarefaSpringMongoDBRepository.save(tarefa);
		} catch (DataIntegrityViolationException e) {
			throw APIException.build(HttpStatus.BAD_REQUEST, "Tarefa já cadastrada", e);
		}
		log.info("[finaliza] TarefaInfraRepository - salva");
		return tarefa;
	}

	@Override
	public Optional<Tarefa> buscaTarefaPorId(UUID idTarefa) {
		log.info("[inicia] TarefaInfraRepository - buscaTarefaPorId");
		Optional<Tarefa> tarefaPorId = tarefaSpringMongoDBRepository.findByIdTarefa(idTarefa);
		log.info("[finaliza] TarefaInfraRepository - buscaTarefaPorId");
		return tarefaPorId;
	}

	@Override
	public void desativaTarefasAtiva(UUID idUsuario) {
		log.info("[inicia] TarefaInfraRepository - desativaTarefasAtiva");
		Query query = new Query();
		query.addCriteria(Criteria.where(ID_USUARIO_FIELD).is(idUsuario).and(STATUS_ATIVACAO_FIELD)
				.is(StatusAtivacaoTarefa.ATIVA));
		Update update = new Update().set(STATUS_ATIVACAO_FIELD, StatusAtivacaoTarefa.INATIVA);
		mongoTemplate.updateMulti(query, update, Tarefa.class);
		log.info("[finaliza] TarefaInfraRepository - desativaTarefasAtiva");
	}

	@Override
	public void deletaTarefa(Tarefa tarefa) {
		log.info("[inicia] TarefaInfraRepository - deleta");
		tarefaSpringMongoDBRepository.delete(tarefa);
		log.info("[finaliza] TarefaInfraRepository - deleta");
	}

	@Override
	public List<Tarefa> listaTodasTarefasDoUsuario(UUID idUsuario) {
		log.info("[inicia] TarefaInfraRepository - listaTodasTarefasDoUsuario");
		List<Tarefa> tarefas = tarefaSpringMongoDBRepository.findAllByIdUsuario(idUsuario);
		log.info("[finaliza] TarefaInfraRepository - listaTodasTarefasDoUsuario");
		return tarefas;
	}
}
