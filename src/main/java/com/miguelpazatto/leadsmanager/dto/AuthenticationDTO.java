package com.miguelpazatto.leadsmanager.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationDTO(
		@NotBlank(message = "Usuário ou senha inválidos") String login,
		@NotBlank(message = "Usuário ou senha inválidos") String password) {

}
