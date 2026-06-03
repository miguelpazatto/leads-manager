package com.miguelpazatto.leadsmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record OptionRequestDTO(
		@Schema(description = "Texto da alternativa", example = "Meu desempenho não aguenta minhas ambições")
		@NotBlank(message = "Opção não descrita") String description,

		@Schema(description = "Peso da opção (Valores permitidos: de 4 a 25)", example = "4")
		@Min(value = 4, message = "O peso mínimo é 4")
		@Max(value = 25, message = "O peso máximo é 25")
		Integer weight,

		@Schema(description = "ID da questão a qual esta opção pertence", example = "1")
		@NotNull(message = "Necessário informar a qual questão a opção pertence") Long questionId) {

}
