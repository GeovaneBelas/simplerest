package com.gbelas.simplerest.api.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import com.gbelas.simplerest.api.enums.TipoPerfil;

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "usuario_generator")
	@SequenceGenerator(name = "usuario_generator", sequenceName = "susuario", initialValue = 1000)
	private Long id;

	@NotBlank
	@Column(name = "login", nullable = false, unique=true)
	private String login;

	@NotBlank
	@Column(name = "senha", nullable = false)
	private String senha;

	@Column(name = "tipo_perfil", nullable = false)
	private TipoPerfil tipoPerfil;

	@Column(name = "ind_ativo")
	private Boolean indAtivo;

	public Usuario() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public TipoPerfil getTipoPerfil() {
		return this.tipoPerfil;
	}

	public void setTipoPerfil(TipoPerfil tipoPerfil) {
		this.tipoPerfil = tipoPerfil;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public String getSenha() {
		return this.senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
