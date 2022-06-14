package dev.wakandaacademy.produdoro.credencial.application.service;

import javax.validation.Valid;

import dev.wakandaacademy.produdoro.credencial.domain.Credencial;
import dev.wakandaacademy.produdoro.usuario.application.api.UsuarioNovoRequest;

public interface CredencialApplicationService {
	void criaNovaCredencial(@Valid UsuarioNovoRequest usuarioNovo);
	Credencial buscaCredencialPorUsuario(String usuario);
}
