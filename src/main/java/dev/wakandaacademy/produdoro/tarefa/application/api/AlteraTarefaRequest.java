package dev.wakandaacademy.produdoro.tarefa.application.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AlteraTarefaRequest {

    @NotBlank(message = "campo descrição não pode estar vazio!")
    private String descricao;
}