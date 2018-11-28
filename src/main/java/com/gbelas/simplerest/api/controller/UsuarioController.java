package com.gbelas.simplerest.api.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gbelas.simplerest.api.enums.Operation;
import com.gbelas.simplerest.api.model.Usuario;
import com.gbelas.simplerest.api.response.Response;
import com.gbelas.simplerest.api.security.jwt.JwtTokenUtil;
import com.gbelas.simplerest.api.service.UsuarioService;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "*")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	protected JwtTokenUtil jwtTokenUtil;

	private boolean validateCreateOrUpdateUser(Usuario u, BindingResult result, Operation operation) {
		if (u.getId() == null && (operation.equals(Operation.UPDATE) || operation.equals(Operation.DELETE)
				|| operation.equals(Operation.SELECT_ID))) {
			result.addError(new ObjectError("Usuario", "Id no information"));
		}
		if (u.getLogin() == null && (operation.equals(Operation.INSERT) || operation.equals(Operation.UPDATE))) {
			result.addError(new ObjectError("Usuario", "Login no information"));
		}
		if (u.getSenha() == null && (operation.equals(Operation.INSERT) || operation.equals(Operation.UPDATE))) {
			result.addError(new ObjectError("Usuario", "Senha no information"));
		}

		return result.hasErrors();
	}

	@PostMapping()
	@PreAuthorize("hasAnyRole('ADM')")
	public ResponseEntity<Response<Usuario>> create(HttpServletRequest request, @RequestBody Usuario usuario,
			BindingResult result) {
		Response<Usuario> response = new Response<Usuario>();
		try {
			System.out.println("testes");
			if (validateCreateOrUpdateUser(usuario, result, Operation.INSERT)) {
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}

			usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
			usuario.setIndAtivo(true);

			response.setData(usuarioService.createOrUpdate(usuario));
		} catch (DuplicateKeyException dE) {
			response.getErrors().add("Login already registered !");
			return ResponseEntity.badRequest().body(response);
		} catch (Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	@PutMapping()
	@PreAuthorize("hasAnyRole('ADM')")
	public ResponseEntity<Response<Usuario>> update(HttpServletRequest request, @RequestBody Usuario u,
			BindingResult result) {
		Response<Usuario> response = new Response<Usuario>();
		try {
			if (validateCreateOrUpdateUser(u, result, Operation.UPDATE)) {
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}

			u.setSenha(passwordEncoder.encode(u.getSenha()));

			response.setData(usuarioService.createOrUpdate(u));
		} catch (Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('ADM')")
	public ResponseEntity<Response<Usuario>> findById(@PathVariable("id") Long id) {
		Response<Usuario> response = new Response<Usuario>();

		Usuario user = null;

		if (id != null) {
			Optional<Usuario> userOptional = usuarioService.findById(id);
			user = userOptional.get();
		}

		if (user != null) {
			response.setData(user);
			return ResponseEntity.ok(response);
		} else {
			response.getErrors().add("Register not found id:" + id);
			return ResponseEntity.badRequest().body(response);
		}

	}

	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ADM')")
	public ResponseEntity<Response<String>> delete(HttpServletRequest request, @PathVariable("id") Long id) {
		Response<String> response = new Response<String>();

		Usuario user = null;

		if (id != null) {
			Optional<Usuario> userOptional = usuarioService.findById(id);
			user = userOptional.get();
		}

		if (user != null) {
			if (jwtTokenUtil.getUsernameFromToken(request.getHeader("Authorization")).equals(user.getLogin())) {
				response.getErrors().add("You can not remove your own access");
				return ResponseEntity.badRequest().body(response);
			} else {
				usuarioService.delete(id);
				response.setData("OK");
				return ResponseEntity.ok(response);
			}
		} else {
			response.getErrors().add("Register not found id:" + id);
			return ResponseEntity.badRequest().body(response);
		}
	}

	@GetMapping(value = "{page}/{count}")
	@PreAuthorize("hasAnyRole('ADM')")
	public ResponseEntity<Response<Page<Usuario>>> findAll(@PathVariable int page, @PathVariable int count) {
		Response<Page<Usuario>> response = new Response<Page<Usuario>>();

		Page<Usuario> uPage = usuarioService.findAll(page, count);
		response.setData(uPage);

		return ResponseEntity.ok(response);
	}

}
