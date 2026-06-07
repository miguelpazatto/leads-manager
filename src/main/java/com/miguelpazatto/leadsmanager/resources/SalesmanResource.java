package com.miguelpazatto.leadsmanager.resources;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miguelpazatto.leadsmanager.dto.SalesmanDTO;
import com.miguelpazatto.leadsmanager.dto.SalesmanUpdateDTO;
import com.miguelpazatto.leadsmanager.services.SalesmanService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/salesman")
@Tag(name = "Salesmen", description = "Endpoints para gerenciamento de Vendedores")
@SecurityRequirement(name = "bearer-key")
public class SalesmanResource {

	@Autowired
	private SalesmanService service;
	
	@GetMapping
	@Operation(summary = "Devolve uma lista com todos os Vendedores")
	@ApiResponse(responseCode = "200", description = "Lista devolvida com sucesso")
	public ResponseEntity<List<SalesmanDTO>> findAll() {
		List<SalesmanDTO> salesman = service.findAll();
		return ResponseEntity.ok().body(salesman);
	}
	
	@GetMapping(value = "/{id}")
	@Operation(summary = "Devolve o Vendedor correspondente ao ID enviado")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Vendedor encontrado com sucesso"),
			@ApiResponse(responseCode = "404", description = "ID do Vendedor não encontrado")
	})
	public ResponseEntity<SalesmanDTO> findById(@PathVariable Long id) {
		SalesmanDTO obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Deleta um Vendedor", description = "Deleta um vendedor no banco de dados")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Vendedor deletado com sucesso"),
			@ApiResponse(responseCode = "404", description = "ID do Vendedor não foi encontrado"),
			@ApiResponse(responseCode = "400", description = "Houve violação da integridade do banco de dados")
	})
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/{id}")
	@Operation(summary = "Altera um Vendedor", description = "Substitui os dados de um vendedor no banco de dados")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Vendedor alterado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Erro de validação (dados enviados incorretos)"),
			@ApiResponse(responseCode = "404", description = "ID do Vendedor não foi encontrado")
	})
	public ResponseEntity<SalesmanDTO> update(@PathVariable Long id, @RequestBody @Valid SalesmanUpdateDTO data) {
		SalesmanDTO dto = service.update(id, data);
		return ResponseEntity.ok().body(dto);
	}
	
	
}
