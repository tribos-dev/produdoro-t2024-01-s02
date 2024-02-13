package dev.wakandaacademy.produdoro.tarefa.application.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlteraTarefaRequest {
    private String descricao;
}
