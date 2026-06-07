package com.miguelpazatto.leadsmanager.resources;

import java.net.URI;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.miguelpazatto.leadsmanager.dto.AuthenticationDTO;
import com.miguelpazatto.leadsmanager.dto.RegisterDTO;
import com.miguelpazatto.leadsmanager.dto.TokenDTO;
import com.miguelpazatto.leadsmanager.dto.UserResponseDTO;
import com.miguelpazatto.leadsmanager.entities.User;
import com.miguelpazatto.leadsmanager.infra.security.TokenService;
import com.miguelpazatto.leadsmanager.repositories.UserRepository;
import com.miguelpazatto.leadsmanager.services.AuthorizationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/auth")
@Tag(name = "Autenticação", description = "Endpoint para gerar Token JWT de acesso")
public class AuthenticationResource {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private AuthorizationService authorizationService;
	
	@PostMapping("/login")
	@Operation(summary = "Realiza o login", description = "Use as credenciais de visitante para testar a API.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Login realizado com sucesso (Token JWT devolvido no corpo da resposta)"),
			@ApiResponse(responseCode = "400", description = "Erro de validação (Campos ausentes ou mal formatados)"),
			@ApiResponse(responseCode = "403", description = "Credenciais inválidas (E-mail ou senha incorretos)")
	})
	public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
		var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
		var auth = authenticationManager.authenticate(usernamePassword);
		
		var token = tokenService.generateToken((User) auth.getPrincipal());
		
		return ResponseEntity.ok(new TokenDTO(token));
	}

	@Hidden
	@PostMapping("/register")
	public ResponseEntity register(@RequestBody @Valid RegisterDTO data, UriComponentsBuilder uriBuilder) {
		if (repository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();
		
		User newUser = authorizationService.register(data);
		
		URI uri = uriBuilder.path("/users/{id}").buildAndExpand(newUser.getId()).toUri();
		
		return ResponseEntity.created(uri).body(new UserResponseDTO(newUser));
		
	}
	
}
