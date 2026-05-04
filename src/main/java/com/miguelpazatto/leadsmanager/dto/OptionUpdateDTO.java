package com.miguelpazatto.leadsmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record OptionUpdateDTO(
		@NotBlank(message = "Opção não descrita") String description,
		@Positive(message = "Obrigatório atribuir valor positivo a opção") Integer weight) {

}
