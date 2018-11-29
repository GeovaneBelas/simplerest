package com.gbelas.simplerest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.gbelas.simplerest.api.enums.TipoPerfil;
import com.gbelas.simplerest.api.model.Usuario;
import com.gbelas.simplerest.api.repository.UsuarioRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimplerestApplicationTests {
	@Autowired
	UsuarioRepository repository;

	@Autowired
	PasswordEncoder passwordEncoder;

	Usuario user;

	@Before
	public void setUp() {
		user = repository.findByLogin("teste");

		if (user != null) {
			repository.deleteById(user.getId());
		}

		user = new Usuario();
		user.setLogin("teste");
		user.setSenha(passwordEncoder.encode("123456"));
		user.setTipoPerfil(TipoPerfil.RES);
		user.setIndAtivo(true);

		repository.save(user);

	}

	@Test
	public void countByTipoRES() {
		Usuario filter = new Usuario();
		filter.setTipoPerfil(TipoPerfil.RES);

		Example<Usuario> example = Example.of(filter);

		assertThat(repository.count(example)).isEqualTo(2L);
	}
	@Test
	public void countByTipoADM() {
		Usuario filter = new Usuario();
		filter.setTipoPerfil(TipoPerfil.ADM);

		Example<Usuario> example = Example.of(filter);

		assertThat(repository.count(example)).isEqualTo(1L);
	}

}
