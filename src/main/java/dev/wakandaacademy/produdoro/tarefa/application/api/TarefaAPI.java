package dev.wakandaacademy.produdoro.tarefa.application.api;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/tarefa")
public interface TarefaAPI {
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    TarefaIdResponse postNovaTarefa(@RequestBody @Valid TarefaRequest tarefaRequest);

    @GetMapping("/{idTarefa}")
    @ResponseStatus(code = HttpStatus.OK)
    TarefaDetalhadoResponse detalhaTarefa(@RequestHeader(name = "Authorization", required = true) String token,
                                          @PathVariable UUID idTarefa);

    @PatchMapping("/{idTarefa}/imcrementa-pomodoro")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    void incrementaPomodoro(@RequestHeader(name = "Authorization") String token,
                            @PathVariable UUID idTarefa);

    @DeleteMapping("/deleta/{idTarefa}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    void deleteTarefa(@RequestHeader(name = "Authorization", required = true) String token,
                      @PathVariable UUID idTarefa);

    @GetMapping("/usuario/{idUsuario}")
    @ResponseStatus(code = HttpStatus.OK)
    List<TarefaDetalhadoResponse> listaTodasTarefasDoUsuario(@RequestHeader(name = "Authorization", required = true) String token,
                                                             @PathVariable UUID idUsuario);

    @PatchMapping("/{idTarefa}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    void alteraTarefa(@RequestHeader(name = "Authorization", required = true) String token,
                      @PathVariable UUID idTarefa, @RequestBody @Valid AlteraTarefaRequest alteraTarefaRequest);

}


