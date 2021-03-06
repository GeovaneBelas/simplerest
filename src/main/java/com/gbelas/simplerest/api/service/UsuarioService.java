package com.gbelas.simplerest.api.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.gbelas.simplerest.api.model.Usuario;


@Component
public interface UsuarioService {

	Usuario findByLogin(String login);
	
	Usuario createOrUpdate(Usuario usuario);
	
	Optional<Usuario> findById(Long id);
	
	void delete(Long id);
	
	Page<Usuario> findAll(String search, int page, int limit,Direction direction, String propertie);
}
