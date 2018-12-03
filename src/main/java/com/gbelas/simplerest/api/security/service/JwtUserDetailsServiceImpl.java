package com.gbelas.simplerest.api.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gbelas.simplerest.api.model.Usuario;
import com.gbelas.simplerest.api.security.jwt.JwtUserFactory;
import com.gbelas.simplerest.api.service.UsuarioService;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioService usuarioService;

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

		Usuario u = usuarioService.findByLogin(login);
		if (u == null) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", login));
		}else if (!u.getIndAtivo()) {
				throw new UsernameNotFoundException(String.format("User '%s' not have access to aplicattion.", login));
		} else {
			return JwtUserFactory.create(u);
		}
	}
}