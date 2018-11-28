package com.gbelas.simplerest.api.security.jwt;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.gbelas.simplerest.api.enums.TipoPerfil;
import com.gbelas.simplerest.api.model.Usuario;

public class JwtUserFactory {
	private JwtUserFactory() {
	}

	public static JwtUser create(Usuario user) {
		return new JwtUser(user.getId(), user.getLogin(), user.getSenha(),
				mapToGrantedAuthorities(user.getTipoPerfil()));
	}

	private static List<GrantedAuthority> mapToGrantedAuthorities(TipoPerfil t) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_"+t.toString()));
		return authorities;
	}
}
