package com.gbelas.simplerest.api.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.gbelas.simplerest.api.model.Usuario;
import com.gbelas.simplerest.api.repository.UsuarioRepository;
import com.gbelas.simplerest.api.service.UsuarioService;

@Component
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	public Usuario findByLogin(String login) {
		return this.usuarioRepository.findByLogin(login);
	}

	public Usuario createOrUpdate(Usuario user) {
		return this.usuarioRepository.save(user);
	}

	public Optional<Usuario>findById(Long id) {
		return this.usuarioRepository.findById(id);
	}

	public void delete(Long id) {
		this.usuarioRepository.deleteById(id);
	}

	public Page<Usuario> findAll(int page, int count) {
		Pageable pages = PageRequest.of(page, count);
		
		return this.usuarioRepository.findAll(pages);
	}
}
