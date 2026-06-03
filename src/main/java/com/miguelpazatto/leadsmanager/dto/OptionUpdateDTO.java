package com.miguelpazatto.leadsmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record OptionUpdateDTO(
		@Schema(description = "Texto novo da alternativa", example = "Meu principal problema é não saber arrumar a rotina")
		@NotBlank(message = "Opção não descrita") String description,

		@Schema(description = "Peso da opção (Valores permitidos: de 4 a 25)", example = "4")
		@Min(value = 4, message = "O peso mínimo é 4")
		@Max(value = 25, message = "O peso máximo é 25")
		Integer weight) {

}
