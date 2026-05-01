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

import com.miguelpazatto.leadsmanager.dto.OptionIdDTO;
import com.miguelpazatto.leadsmanager.dto.OptionRequestDTO;
import com.miguelpazatto.leadsmanager.entities.Option;
import com.miguelpazatto.leadsmanager.services.OptionService;

@RestController
@RequestMapping(value = "/options")
public class OptionResource {

	@Autowired
	private OptionService service;
	
	@GetMapping
	public ResponseEntity<List<OptionRequestDTO>> findAll() {
		List<OptionRequestDTO> options = service.findAll();
		return ResponseEntity.ok().body(options);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<OptionRequestDTO> findById(@PathVariable Long id) {
		OptionRequestDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}
	
	@PostMapping
	public ResponseEntity<OptionIdDTO> insert(@RequestBody Option obj) {
		OptionIdDTO dto = service.insert(obj);
		URI	uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.id()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<OptionRequestDTO> update(@PathVariable Long id, @RequestBody Option obj) {
		OptionRequestDTO dto = service.update(id, obj);
		return ResponseEntity.ok().body(dto);
	}
	
	
}
