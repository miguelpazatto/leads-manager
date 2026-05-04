package com.miguelpazatto.leadsmanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SalesmanUpdateDTO(
		@NotBlank(message = "Nome do vendedor é obrigatório") String name, 
		@Email(message = "Insira um email válido") String email, 
		@Pattern(regexp = "^\\d+$", message = "O campo deve conter apenas números") 
		@Size(min = 11, message = "Telefone deve conter ao menos 11 caracteres (2 DDD + 9 Linha Celular)")
		String phone) {

}
