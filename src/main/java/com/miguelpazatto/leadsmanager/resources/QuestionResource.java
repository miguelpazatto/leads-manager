package com.miguelpazatto.leadsmanager.resources;

import java.net.URI;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

import com.miguelpazatto.leadsmanager.dto.QuestionDTO;
import com.miguelpazatto.leadsmanager.dto.QuestionRequestDTO;
import com.miguelpazatto.leadsmanager.services.QuestionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/questions")
@Tag(name =  "Questions", description = "Endpoints para gerenciamento de Questões")
@SecurityRequirement(name = "bearer-key")
public class QuestionResource {

	@Autowired
	private QuestionService service;
	
	@GetMapping
	@Operation(summary = "Devolve uma lista com todas as Questões")
	@ApiResponse(responseCode = "200", description = "Lista devolvida com sucesso")
	public ResponseEntity<List<QuestionDTO>> findAll() {
		List<QuestionDTO> questions = service.findAll();
		return ResponseEntity.ok().body(questions);
	}
	
	@GetMapping(value = "/{id}")
	@Operation(summary = "Devolve a Questão correspondente ao ID enviado")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Questão encontrada com sucesso"),
			@ApiResponse(responseCode = "404", description = "ID da Questão não foi encontrado")
	})
	public ResponseEntity<QuestionDTO> findById(@PathVariable Long id) {
		QuestionDTO obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@PostMapping
	@Operation(summary = "Cria uma nova Questão", description = "Insere uma nova questão no banco de dados")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Questão criada com sucesso"),
			@ApiResponse(responseCode = "400", description = "Erro de validação (dados enviados incorretos)"),
			@ApiResponse(responseCode = "404", description = "ID da Questão não foi encontrada")
	})
	public ResponseEntity<QuestionDTO> insert(@RequestBody @Valid QuestionRequestDTO data) {
		QuestionDTO dto = service.insert(data);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.id()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Deleta uma Questão", description = "Deleta uma questão no banco de dados vinculada a uma questão")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Questão deletada com sucesso"),
			@ApiResponse(responseCode = "404", description = "ID da Questão não foi encontrado"),
			@ApiResponse(responseCode = "400", description = "Houve violação da integridade do banco de dados")
	})
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/{id}")
	@Operation(summary = "Altera uma Questão com ID correspondente ao enviado")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Questão alterada com sucesso"),
			@ApiResponse(responseCode = "404", description = "ID da Questão não foi encontrado"),
			@ApiResponse(responseCode = "400", description = "Erro de validação (dados enviados incorretos)")
	})
	public ResponseEntity<QuestionDTO> update(@PathVariable Long id, @RequestBody QuestionRequestDTO data) {
		QuestionDTO dto = service.update(id, data);
		return ResponseEntity.ok().body(dto);
	}
	
}
