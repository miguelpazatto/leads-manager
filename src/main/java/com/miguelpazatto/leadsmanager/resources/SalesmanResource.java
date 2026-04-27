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

import com.miguelpazatto.leadsmanager.dto.SalesmanDTO;
import com.miguelpazatto.leadsmanager.entities.Salesman;
import com.miguelpazatto.leadsmanager.services.SalesmanService;

@RestController
@RequestMapping(value = "/salesman")
public class SalesmanResource {

	@Autowired
	private SalesmanService service;
	
	@GetMapping
	public ResponseEntity<List<SalesmanDTO>> findAll() {
		List<SalesmanDTO> salesman = service.findAll();
		return ResponseEntity.ok().body(salesman);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<SalesmanDTO> findById(@PathVariable Long id) {
		SalesmanDTO obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@PostMapping
	public ResponseEntity<SalesmanDTO> insert(@RequestBody Salesman obj) {
		SalesmanDTO dto = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.id()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<SalesmanDTO> update(@PathVariable Long id, @RequestBody Salesman obj) {
		SalesmanDTO dto = service.update(id, obj);
		return ResponseEntity.ok().body(dto);
	}
	
	
}
