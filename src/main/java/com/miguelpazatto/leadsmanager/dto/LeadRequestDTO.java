package com.miguelpazatto.leadsmanager.dto;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record LeadRequestDTO(@NotBlank(message = "Nome obrigatório") String name,
		@Email(message = "Email inválido") String email, 
		@NotBlank String phone, 
		@NotEmpty(message = "Formulário deve ser preenchido corretamente") List<Long> optionsId ) {
}