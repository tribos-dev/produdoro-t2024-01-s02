package dev.wakandaacademy.produdoro.usuario.application.service;

import javax.validation.Valid;

import dev.wakandaacademy.produdoro.config.security.service.TokenService;
import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.usuario.application.repository.UsuarioRepository;
import dev.wakandaacademy.produdoro.usuario.domain.StatusUsuario;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import dev.wakandaacademy.produdoro.credencial.application.service.CredencialService;
import dev.wakandaacademy.produdoro.pomodoro.application.service.PomodoroService;
import dev.wakandaacademy.produdoro.usuario.application.api.UsuarioCriadoResponse;
import dev.wakandaacademy.produdoro.usuario.application.api.UsuarioNovoRequest;
import dev.wakandaacademy.produdoro.usuario.domain.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Objects;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class UsuarioApplicationService implements UsuarioService {
	private final PomodoroService pomodoroService;
	private final CredencialService credencialService;
	private final UsuarioRepository usuarioRepository;
	private final TokenService tokenService;

	@Override
	public UsuarioCriadoResponse criaNovoUsuario(@Valid UsuarioNovoRequest usuarioNovo) {
		log.info("[inicia] UsuarioApplicationService - criaNovoUsuario");
		var configuracaoPadrao = pomodoroService.getConfiguracaoPadrao();
		credencialService.criaNovaCredencial(usuarioNovo);
		var usuario = new Usuario(usuarioNovo,configuracaoPadrao);
		usuarioRepository.salva(usuario);
		log.info("[finaliza] UsuarioApplicationService - criaNovoUsuario");
		return new UsuarioCriadoResponse(usuario);
	}


	@Override
	public UsuarioCriadoResponse buscaUsuarioPorId(UUID idUsuario) {
		log.info("[inicia] UsuarioApplicationService - buscaUsuarioPorId");
		Usuario usuario = usuarioRepository.buscaUsuarioPorId(idUsuario);
		log.info("[finaliza] UsuarioApplicationService - buscaUsuarioPorId");
		return new UsuarioCriadoResponse(usuario);
	}

	@Override
	public void alterarStatusParaFoco(String token, UUID idUsuario) {
		log.info("[inicia] UsuarioApplicationService - alterarStatusParaFoco");
		var email = tokenService.getUsuarioByBearerToken(token)
				.orElseThrow(() -> APIException.build(HttpStatus.UNAUTHORIZED, "Credencial de autenticação não é valida"));

		var usuarioToken = usuarioRepository.buscaUsuarioPorEmail(email);
		var usuario = usuarioRepository.buscaUsuarioPorId(idUsuario);

		if (!Objects.equals(usuario.getEmail(), usuarioToken.getEmail()))
			throw APIException.build(HttpStatus.UNAUTHORIZED, "O token informado não pertence ao usuário");

		if (usuario.getStatus() != StatusUsuario.FOCO)  usuarioRepository.alterarStatusParaFoco(usuario);

		log.info("[finaliza] UsuarioApplicationService - alterarStatusParaFoco");

	}


}
