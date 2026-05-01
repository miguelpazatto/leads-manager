package com.miguelpazatto.leadsmanager.resources;

import java.net.URI;
import java.util.List;

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

@RestController
@RequestMapping(value = "/options")
public class OptionResource {

	@Autowired
	private OptionService service;
	
	@GetMapping
	public ResponseEntity<List<OptionResponseDTO>> findAll() {
		List<OptionResponseDTO> options = service.findAll();
		return ResponseEntity.ok().body(options);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<OptionResponseDTO> findById(@PathVariable Long id) {
		OptionResponseDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}
	
	@PostMapping
	public ResponseEntity<OptionResponseDTO> insert(@RequestBody OptionRequestDTO obj) {
		OptionResponseDTO dto = service.insert(obj);
		URI	uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.id()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<OptionResponseDTO> update(@PathVariable Long id, @RequestBody OptionUpdateDTO obj) {
		OptionResponseDTO dto = service.update(id, obj);
		return ResponseEntity.ok().body(dto);
	}
	
	
}
