package com.gbelas.simplerest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gbelas.simplerest.api.enums.TipoPerfil;
import com.gbelas.simplerest.api.model.Usuario;
import com.gbelas.simplerest.api.repository.UsuarioRepository;

@SpringBootApplication
public class SimplerestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimplerestApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			initUsers(usuarioRepository, passwordEncoder);
		};

	}

	private void initUsers(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {

		for (TipoPerfil tp : TipoPerfil.values()) {
			String login = "user_" + tp.name().toLowerCase();
			Usuario user = usuarioRepository.findByLogin(login);
			if (user == null) {
				user = new Usuario();
				user.setLogin(login);
				user.setSenha(passwordEncoder.encode("123456"));
				user.setTipoPerfil(tp);
				user.setIndAtivo(true);

				usuarioRepository.save(user);
			}
		}

	}

}
