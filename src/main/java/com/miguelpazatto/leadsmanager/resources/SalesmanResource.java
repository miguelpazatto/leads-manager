package com.miguelpazatto.leadsmanager.resources;

import java.util.List;

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
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<SalesmanDTO> update(@PathVariable Long id, @RequestBody @Valid SalesmanUpdateDTO data) {
		SalesmanDTO dto = service.update(id, data);
		return ResponseEntity.ok().body(dto);
	}
	
	
}
