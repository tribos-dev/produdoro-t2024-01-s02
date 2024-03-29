package dev.wakandaacademy.produdoro.usuario.application.api;

import javax.validation.Valid;

import dev.wakandaacademy.produdoro.config.security.service.TokenService;
import dev.wakandaacademy.produdoro.handler.APIException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import dev.wakandaacademy.produdoro.usuario.application.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.UUID;

@RestController
@Validated
@Log4j2
@RequiredArgsConstructor
public class UsuarioController implements UsuarioAPI {
	private final UsuarioService usuarioAppplicationService;
	private final TokenService tokenService;

	@Override
	public UsuarioCriadoResponse postNovoUsuario(@Valid UsuarioNovoRequest usuarioNovo) {
		log.info("[inicia] UsuarioController - postNovoUsuario");
		UsuarioCriadoResponse usuarioCriado = usuarioAppplicationService.criaNovoUsuario(usuarioNovo);
		log.info("[finaliza] UsuarioController - postNovoUsuario");
		return usuarioCriado;
	}

	@Override
	public UsuarioCriadoResponse buscaUsuarioPorId(UUID idUsuario) {
		log.info("[inicia] UsuarioController - buscaUsuarioPorId");
		log.info("[idUsuario] {}", idUsuario);
		UsuarioCriadoResponse buscaUsuario = usuarioAppplicationService.buscaUsuarioPorId(idUsuario);
		log.info("[finaliza] UsuarioController - buscaUsuarioPorId");
		return buscaUsuario;
	}

	@Override
	public void usuarioMudaStatusParaPausaCurta(String token, UUID idUsuario) {
		log.info("[inicia] UsuarioController - usuarioMudaStatusParaPausaCurta");
		String emailUsuario = tokenService.getUsuarioByBearerToken(token)
				.orElseThrow(() -> APIException.build(HttpStatus.FORBIDDEN, "Token inválido!"));
		usuarioAppplicationService.mudaStatusParaPausaCurta(idUsuario, emailUsuario);
		log.info("[finaliza] UsuarioController - usuarioMudaStatusParaPausaCurta");
	}

	@Override
	public void alteraStatusParaFoco(String token, UUID idUsuario) {
		log.info("[inicia] UsuarioController - alterarStatusParaFoco");
		log.info("[idUsuario] {}", idUsuario);
		String usuario = tokenService.getUsuarioByBearerToken(token)
				.orElseThrow(() -> APIException.build(HttpStatus.UNAUTHORIZED, "Credencial de autenticação não é valida"));
		usuarioAppplicationService.alteraStatusParaFoco(usuario, idUsuario);
		log.info("[finaliza] UsuarioController - alterarStatusParaFoco");
	}

	@Override
	public void patchAlteraStatusParaPausaLonga(String token, UUID idUsuario) {
		log.info("[inicia] UsuarioController - patchAlteraStatusParaPausaLonga");
		log.info("[idUsuario] {}", idUsuario);
		String usuarioEmail = tokenService.getUsuarioByBearerToken(token)
				.orElseThrow(() -> APIException.build(HttpStatus.FORBIDDEN, "Token inválido."));
		usuarioAppplicationService.alteraStatusParaPausaLonga(usuarioEmail, idUsuario);
		log.info("[finaliza] UsuarioController - patchAlteraStatusParaPausaLonga");
	}
}
