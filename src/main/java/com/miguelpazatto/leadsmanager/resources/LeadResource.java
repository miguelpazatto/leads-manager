package com.miguelpazatto.leadsmanager.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miguelpazatto.leadsmanager.dto.LeadPublicDTO;
import com.miguelpazatto.leadsmanager.dto.LeadSalesDTO;
import com.miguelpazatto.leadsmanager.services.LeadService;

@RestController
@RequestMapping(value = "/leads")
public class LeadResource {

	@Autowired
	private LeadService service;
	
	@GetMapping
	public ResponseEntity<List<LeadSalesDTO>> findAll() {
		List<LeadSalesDTO> leads =  service.findAll();
		return ResponseEntity.ok().body(leads);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<LeadSalesDTO> findById(@PathVariable Long id) {
		LeadSalesDTO obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@GetMapping(value = "/public/{id}")
	public ResponseEntity<LeadPublicDTO> publicFindById(@PathVariable Long id) {
		LeadPublicDTO obj = service.publicFindById(id);
		return ResponseEntity.ok().body(obj);
	}
	
}
