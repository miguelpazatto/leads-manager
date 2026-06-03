package com.miguelpazatto.leadsmanager.resources;

import java.net.URI;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.miguelpazatto.leadsmanager.dto.OptionRequestDTO;
import com.miguelpazatto.leadsmanager.dto.OptionResponseDTO;
import com.miguelpazatto.leadsmanager.dto.OptionUpdateDTO;
import com.miguelpazatto.leadsmanager.services.OptionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/options")
@Tag(name = "Options", description = "Endpoints para gerenciamento das Opções (Alternativas) das questões")
public class OptionResource {

	@Autowired
	private OptionService service;

	@GetMapping
	@Operation(summary = "Devolve uma lista com todas as Opções")
	@ApiResponse(responseCode = "200", description = "Lista devolvida com sucesso")
	public ResponseEntity<List<OptionResponseDTO>> findAll() {
		List<OptionResponseDTO> options = service.findAll();
		return ResponseEntity.ok().body(options);
	}
	
	@GetMapping(value = "/{id}")
	@Operation(summary = "Devolve a Opção correspondente ao ID enviado")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Opção encontrada com sucesso"),
			@ApiResponse(responseCode = "404", description = "ID da Opção não foi encontrado")
	})
	public ResponseEntity<OptionResponseDTO> findById(@PathVariable Long id) {
		OptionResponseDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}
	
	@PostMapping
	@Operation(summary = "Cria uma nova Opção", description = "Insere uma nova opção no banco de dados vinculada a uma questão existente.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Opção criada com sucesso"),
			@ApiResponse(responseCode = "400", description = "Erro de validação (dados enviados incorretos)"),
			@ApiResponse(responseCode = "404", description = "A Questão atrelada a esta opção não foi encontrada")
	})
	public ResponseEntity<OptionResponseDTO> insert(@RequestBody @Valid OptionRequestDTO obj) {
		OptionResponseDTO dto = service.insert(obj);
		URI	uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.id()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}

	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Deleta uma opção", description = "Deleta uma opção no banco de dados vinculada a uma questão")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Opção deletada com sucesso"),
			@ApiResponse(responseCode = "404", description = "ID da Opção não foi encontrado"),
			@ApiResponse(responseCode = "400", description = "Houve violação da integridade do banco de dados")
	})
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/{id}")
	@Operation(summary = "Altera uma Opção com ID correspondente ao enviado")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Opção alterada com sucesso"),
			@ApiResponse(responseCode = "404", description = "ID da Opção não foi encontrado"),
			@ApiResponse(responseCode = "400", description = "Erro de validação (dados enviados incorretos)")
	})
	public ResponseEntity<OptionResponseDTO> update(@PathVariable Long id, @RequestBody @Valid OptionUpdateDTO obj) {
		OptionResponseDTO dto = service.update(id, obj);
		return ResponseEntity.ok().body(dto);
	}

}
