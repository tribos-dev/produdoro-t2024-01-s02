package dev.wakandaacademy.produdoro.usuario.application.service;

import dev.wakandaacademy.produdoro.usuario.application.api.UsuarioCriadoResponse;
import dev.wakandaacademy.produdoro.usuario.application.api.UsuarioNovoRequest;

import java.util.UUID;

public interface UsuarioService {
	UsuarioCriadoResponse criaNovoUsuario(UsuarioNovoRequest usuarioNovo);
    UsuarioCriadoResponse buscaUsuarioPorId(UUID idUsuario);
<<<<<<< HEAD
    void mudaStatusParaPausaCurta(UUID idUsuario, String usuario);
=======
    void alteraStatusParaFoco(String usuario, UUID idUsuario);
    void alteraStatusParaPausaLonga(String usuarioEmail, UUID idUsuario);
>>>>>>> dev
}
