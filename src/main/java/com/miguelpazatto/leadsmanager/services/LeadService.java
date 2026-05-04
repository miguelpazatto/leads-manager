package com.miguelpazatto.leadsmanager.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.miguelpazatto.leadsmanager.dto.LeadPublicDTO;
import com.miguelpazatto.leadsmanager.dto.LeadRequestDTO;
import com.miguelpazatto.leadsmanager.dto.LeadSalesDTO;
import com.miguelpazatto.leadsmanager.dto.LeadUpdateDTO;
import com.miguelpazatto.leadsmanager.entities.Answer;
import com.miguelpazatto.leadsmanager.entities.Lead;
import com.miguelpazatto.leadsmanager.entities.Option;
import com.miguelpazatto.leadsmanager.entities.Salesman;
import com.miguelpazatto.leadsmanager.repositories.LeadRepository;
import com.miguelpazatto.leadsmanager.repositories.OptionRepository;
import com.miguelpazatto.leadsmanager.services.exceptions.DatabaseException;
import com.miguelpazatto.leadsmanager.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class LeadService {

	@Autowired
	private LeadRepository repository;
	
	@Autowired
	private SalesmanService salesmanService;
	
	@Autowired
	private OptionRepository optionRepository;
	
	public List<LeadSalesDTO> findAll() {
		List<Lead> leads = repository.findAll();
		return leads.stream().map(LeadSalesDTO::new).toList();
	}
	
	public LeadSalesDTO findById(Long id) {
		Optional<Lead> obj = repository.findById(id);
		return obj.map(LeadSalesDTO::new).orElseThrow(() -> new ResourceNotFoundException(id));
	}
	
	public LeadPublicDTO publicFindById(Long id) {
		Optional<Lead> obj = repository.findById(id);
		return obj.map(LeadPublicDTO::new).orElseThrow(() -> new ResourceNotFoundException(id));
	}
	
	public LeadSalesDTO insert(LeadRequestDTO data) {
		Salesman salesman = salesmanService.assignSalesman();
		Lead obj = new Lead(null, data.name(), data.email(), data.phone(), salesman);
		
		List<Answer> answers = new ArrayList<>();
		
		for (Long o : data.optionsId()) {
			Option option = optionRepository.findById(o).orElseThrow(() -> new ResourceNotFoundException(o));
			answers.add(new Answer(option, obj));
		}
		
		obj.setOptions(answers);
		
		return new LeadSalesDTO(repository.save(obj));
	}
	
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException(id);
		}
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	public LeadSalesDTO update(Long id, LeadUpdateDTO data) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException(id);
		}
		try {
			Lead entity = repository.getReferenceById(id);
			updateData(entity, data);
			return new LeadSalesDTO(repository.save(entity));	
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}

	}
	
	private void updateData(Lead entity, LeadUpdateDTO obj) {
		entity.setName(obj.name());
		entity.setEmail(obj.email());
		entity.setPhone(obj.phone());
	}
	
	@Transactional
	public void markAsContacted(Long id) {
		try {
			Lead obj = repository.getReferenceById(id);
			obj.markAsContacted();	
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}

	}
	
}
