package com.miguelpazatto.leadsmanager.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class AuthenticationResource {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private AuthorizationService authorizationService;
	
	@PostMapping("/login")
	public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
		var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
		var auth = authenticationManager.authenticate(usernamePassword);
		
		var token = tokenService.generateToken((User) auth.getPrincipal());
		
		return ResponseEntity.ok(new TokenDTO(token));
	}
	
	@PostMapping("/register")
	public ResponseEntity register(@RequestBody @Valid RegisterDTO data, UriComponentsBuilder uriBuilder) {
		if (repository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();
		
		User newUser = authorizationService.register(data);
		
		URI uri = uriBuilder.path("/users/{id}").buildAndExpand(newUser.getId()).toUri();
		
		return ResponseEntity.created(uri).body(new UserResponseDTO(newUser));
		
	}
	
}
