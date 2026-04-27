package com.miguelpazatto.leadsmanager.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miguelpazatto.leadsmanager.dto.LeadPublicDTO;
import com.miguelpazatto.leadsmanager.dto.LeadSalesDTO;
import com.miguelpazatto.leadsmanager.entities.Lead;
import com.miguelpazatto.leadsmanager.repositories.LeadRepository;

import jakarta.persistence.EntityNotFoundException;

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
	
	public LeadSalesDTO insert(Lead obj) {
		return new LeadSalesDTO(repository.save(obj));
	}
	
	public void delete(Long id) {
		//configurar exception
		repository.deleteById(id);
	}
	
	public LeadSalesDTO update(Long id, Lead obj) {
		//configurar exception
			Lead entity = repository.getReferenceById(id);
			updateData(entity, obj);
			return new LeadSalesDTO(repository.save(entity));
		
	}
	
	private void updateData(Lead entity, Lead obj) {
		entity.setName(obj.getName());
		entity.setEmail(obj.getEmail());
		entity.setPhone(obj.getPhone());
		entity.setTotalScore(obj.getTotalScore());
		entity.setLeadStatus(obj.getLeadStatus());
		entity.setLeadClassification(obj.getLeadClassification());
		entity.setAssignedTo(obj.getAssignedTo());
	}
	
}
