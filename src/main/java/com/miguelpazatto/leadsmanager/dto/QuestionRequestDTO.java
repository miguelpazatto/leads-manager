package com.miguelpazatto.leadsmanager.dto;

import jakarta.validation.constraints.NotBlank;

public record QuestionRequestDTO(
		@NotBlank(message = "Enunciado da questão é obrigatório") String statement) {

}
