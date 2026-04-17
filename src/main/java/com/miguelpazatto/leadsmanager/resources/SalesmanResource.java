package com.miguelpazatto.leadsmanager.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miguelpazatto.leadsmanager.entities.Salesman;
import com.miguelpazatto.leadsmanager.services.SalesmanService;

@RestController
@RequestMapping(value = "/salesman")
public class SalesmanResource {

	@Autowired
	private SalesmanService service;
	
	@GetMapping
	public ResponseEntity<List<Salesman>> findAll() {
		List<Salesman> salesman = service.findAll();
		return ResponseEntity.ok().body(salesman);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Salesman> findById(@PathVariable Long id) {
		Salesman obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}
}
