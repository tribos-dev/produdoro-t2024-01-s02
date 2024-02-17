package dev.wakandaacademy.produdoro.tarefa.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import dev.wakandaacademy.produdoro.DataHelper;
import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.usuario.application.repository.UsuarioRepository;
import dev.wakandaacademy.produdoro.usuario.domain.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaIdResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.application.repository.TarefaRepository;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class TarefaApplicationServiceTest {

    //	@Autowired
    @InjectMocks
    TarefaApplicationService tarefaApplicationService;

    //	@MockBean
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

    @Test
    void deveDeletarTarefa() {
        Usuario usuario = DataHelper.createUsuario();
        Tarefa tarefa = DataHelper.createTarefa();

        when(usuarioRepository.buscaUsuarioPorEmail(eq(usuario.getEmail()))).thenReturn(usuario);
        when(tarefaRepository.buscaTarefaPorId(eq(tarefa.getIdTarefa()))).thenReturn(Optional.ofNullable(tarefa));

        tarefaApplicationService.deletaTarefa(usuario.getEmail(), tarefa.getIdTarefa());

        verify(usuarioRepository, times(1)).buscaUsuarioPorEmail(eq(usuario.getEmail()));
        verify(tarefaRepository, times(1)).buscaTarefaPorId(eq(tarefa.getIdTarefa()));
        verify(tarefaRepository, times(1)).deletaTarefa(tarefa);
    }

    @Test
    void naoDeveDeletarTarefa() {
        Usuario usuario = DataHelper.createUsuario();
        Tarefa tarefa = DataHelper.createTarefa();
        UUID idTarefa = tarefa.getIdTarefa();

        when(usuarioRepository.buscaUsuarioPorEmail(eq(usuario.getEmail()))).thenReturn(usuario);
        when(tarefaRepository.buscaTarefaPorId(eq(idTarefa))).thenReturn(Optional.empty());

        APIException ex = assertThrows(APIException.class,
                () -> tarefaApplicationService.deletaTarefa(usuario.getEmail(), idTarefa));

        assertEquals("Tarefa não encontrada!", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusException());
        verify(usuarioRepository, times(1)).buscaUsuarioPorEmail(eq(usuario.getEmail()));
        verify(tarefaRepository, times(1)).buscaTarefaPorId(eq(idTarefa));
        verify(tarefaRepository, never()).deletaTarefa(any());
    }

    public TarefaRequest getTarefaRequest() {
        TarefaRequest request = new TarefaRequest("tarefa 1", UUID.randomUUID(), null, null, 0);
        return request;
    }
}
