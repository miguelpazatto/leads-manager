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

import com.miguelpazatto.leadsmanager.dto.LeadPublicDTO;
import com.miguelpazatto.leadsmanager.dto.LeadRequestDTO;
import com.miguelpazatto.leadsmanager.dto.LeadSalesDTO;
import com.miguelpazatto.leadsmanager.dto.LeadUpdateDTO;
import com.miguelpazatto.leadsmanager.services.LeadService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/leads")
@Tag(name = "Leads", description = "Endpoints para genreciar Leads")
@SecurityRequirement(name = "bearer-key")
public class LeadResource {

	@Autowired
	private LeadService service;
	
	@GetMapping
	@Operation(summary = "Devolve uma lista com todos os Leads")
	@ApiResponse(responseCode = "200", description = "Lista devolvida com sucesso")
	public ResponseEntity<List<LeadSalesDTO>> findAll() {
		List<LeadSalesDTO> leads =  service.findAll();
		return ResponseEntity.ok().body(leads);
	}
	
	@GetMapping(value = "/{id}")
	@Operation(summary = "Devolve o Lead correspondente ao ID enviado para o vendedor")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Opção encontrada com sucesso"),
			@ApiResponse(responseCode = "404", description = "ID da Opção não foi encontrado")
	})
	public ResponseEntity<LeadSalesDTO> findById(@PathVariable Long id) {
		LeadSalesDTO obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@GetMapping(value = "/public/{id}")
	@Operation(summary = "Devolve o Lead correspondente ao ID enviado para o cliente")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lead encontrado com sucesso"),
			@ApiResponse(responseCode = "404", description = "ID do Lead não foi encontrado")
	})
	public ResponseEntity<LeadPublicDTO> publicFindById(@PathVariable Long id) {
		LeadPublicDTO obj = service.publicFindById(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@PostMapping
	@Operation(summary = "Cria um novo Lead", description = "Insere um novo lead no banco de dados vinculado a um vendedor existente.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Lead criado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Erro de validação (dados enviados incorretos)"),
			@ApiResponse(responseCode = "404", description = "A Opção atrelada ao lead não foi encontrada"),
			@ApiResponse(responseCode = "409", description = "Já existe um Lead com o email enviado no bando de dados")
	})
	public ResponseEntity<LeadSalesDTO> insert(@RequestBody @Valid LeadRequestDTO data) {
		LeadSalesDTO dto = service.insert(data);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.id()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Deleta um Lead", description = "Deleta um lead no banco de dados vinculado a um vendedor")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Lead deletada com sucesso"),
			@ApiResponse(responseCode = "404", description = "ID da Lead não foi encontrado"),
			@ApiResponse(responseCode = "400", description = "Houve violação da integridade do banco de dados")
	})
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/{id}")
	@Operation(summary = "Altera um Lead com ID correspondente ao enviado")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lead alterado com sucesso"),
			@ApiResponse(responseCode = "404", description = "ID do Lead não foi encontrado"),
			@ApiResponse(responseCode = "400", description = "Erro de validação (dados enviados incorretos)")
	})
	public ResponseEntity<LeadSalesDTO> update(@PathVariable Long id, @RequestBody @Valid LeadUpdateDTO data) {
		LeadSalesDTO dto = service.update(id, data);
		return ResponseEntity.ok().body(dto);
	}
	
	@PutMapping(value = "/{id}/contacted")
	@Operation(summary = "Marca um Lead existente como contatado")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Lead marcado como contatado"),
			@ApiResponse(responseCode = "404", description = "Lead não encontrado")
	})
	public ResponseEntity<Void> markAsContacted(@PathVariable Long id) {
			service.markAsContacted(id);
			return ResponseEntity.noContent().build();	
	}
	
}
