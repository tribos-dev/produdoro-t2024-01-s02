package dev.wakandaacademy.produdoro.usuario.domain;

import java.util.UUID;

import javax.validation.constraints.Email;

import dev.wakandaacademy.produdoro.handler.APIException;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.pomodoro.domain.ConfiguracaoPadrao;
import dev.wakandaacademy.produdoro.usuario.application.api.UsuarioNovoRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
@Document(collection = "Usuario")
public class Usuario {
	@Id
	private UUID idUsuario;
	@Email
	@Indexed(unique = true)
	private String email;
	private ConfiguracaoUsuario configuracao;
	@Builder.Default
	private StatusUsuario status = StatusUsuario.FOCO;
	@Builder.Default
	private Integer quantidadePomodorosPausaCurta = 0;
	
	public Usuario(UsuarioNovoRequest usuarioNovo, ConfiguracaoPadrao configuracaoPadrao) {
		this.idUsuario = UUID.randomUUID();
		this.email = usuarioNovo.getEmail();
		this.status = StatusUsuario.FOCO;
		this.configuracao = new ConfiguracaoUsuario(configuracaoPadrao);
	}

	public void validaUsuario(UUID idUsuario) {
		if(!this.idUsuario.equals(idUsuario)) {
			throw APIException.build(HttpStatus.UNAUTHORIZED,
					"Credencial de autenticação não é válida!");
		}
	}

	public void mudaParaPausaCurta() {
		this.status = StatusUsuario.PAUSA_CURTA;
	}

	public void alteraStatusFoco(UUID idUsuario) {
		validaUsuario(idUsuario);
		mudaStatusParaFoco();
	}

    private  StatusUsuario mudaStatusParaFoco() {
		if (this.status.equals(StatusUsuario.FOCO)) {
			throw APIException.build(HttpStatus.CONFLICT, "Status do usuário já está em foco");
		}
		return this.status = StatusUsuario.FOCO;
	}
	
    public void validaUsuario(Usuario usuario) {
		if (!this.idUsuario.equals(usuario.getIdUsuario())){
		throw APIException.build(HttpStatus.UNAUTHORIZED, "credencial de autenticação não e valida");
		}
	}

	public void alteraStatusPausaLonga(UUID idUsuario) {
		validaUsuario(idUsuario);
		mudaStatusParaPausaLonga();
	}

	private StatusUsuario mudaStatusParaPausaLonga() {
		if (this.status.equals(StatusUsuario.PAUSA_LONGA)) {
			throw APIException.build(HttpStatus.CONFLICT, "Usuário já está em Pausa Longa.");
		}
		return this.status = StatusUsuario.PAUSA_LONGA;
	}
}
