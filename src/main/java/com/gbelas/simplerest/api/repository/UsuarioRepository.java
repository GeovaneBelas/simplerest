package com.gbelas.simplerest.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gbelas.simplerest.api.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Usuario findByLogin(String login);

}
