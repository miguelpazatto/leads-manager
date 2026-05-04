package com.miguelpazatto.leadsmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record OptionRequestDTO(
		@NotBlank(message = "Opção não descrita") String description,
		@Positive(message = "Obrigatório atribuir valor positivo a opção") 	Integer weight, 
		@NotBlank(message = "Necessário informar a qual questão a opção pertence") Long questionId) {

	
}
