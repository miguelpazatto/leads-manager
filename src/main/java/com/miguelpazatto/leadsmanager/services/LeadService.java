package com.miguelpazatto.leadsmanager.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miguelpazatto.leadsmanager.dto.LeadPublicDTO;
import com.miguelpazatto.leadsmanager.dto.LeadSalesDTO;
import com.miguelpazatto.leadsmanager.entities.Lead;
import com.miguelpazatto.leadsmanager.repositories.LeadRepository;

@Service
public class LeadService {

	@Autowired
	private LeadRepository repository;
	
	public List<LeadSalesDTO> findAll() {
		List<Lead> leads = repository.findAll();
		return leads.stream().map(LeadSalesDTO::new).toList();
	}
	
	public LeadSalesDTO findById(Long id) {
		Optional<Lead> obj = repository.findById(id);
		return obj.map(LeadSalesDTO::new).orElseThrow();
	}
	
	public LeadPublicDTO publicFindById(Long id) {
		Optional<Lead> obj = repository.findById(id);
		return obj.map(LeadPublicDTO::new).orElseThrow();
	}
	
	public Lead insert(Lead obj) {
		return repository.save(obj);
	}
	
}
