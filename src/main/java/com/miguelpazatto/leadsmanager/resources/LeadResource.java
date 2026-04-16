package com.miguelpazatto.leadsmanager.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miguelpazatto.leadsmanager.entities.Lead;
import com.miguelpazatto.leadsmanager.services.LeadService;

@RestController
@RequestMapping(value = "/leads")
public class LeadResource {

	@Autowired
	private LeadService service;
	
	@GetMapping
	public ResponseEntity<List<Lead>> findAll() {
		List<Lead> leads =  service.findAll();
		return ResponseEntity.ok().body(leads);
	}
	
	
}
