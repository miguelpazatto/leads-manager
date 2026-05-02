package com.miguelpazatto.leadsmanager.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miguelpazatto.leadsmanager.dto.LeadPublicDTO;
import com.miguelpazatto.leadsmanager.dto.LeadRequestDTO;
import com.miguelpazatto.leadsmanager.dto.LeadSalesDTO;
import com.miguelpazatto.leadsmanager.entities.Answer;
import com.miguelpazatto.leadsmanager.entities.Lead;
import com.miguelpazatto.leadsmanager.entities.Option;
import com.miguelpazatto.leadsmanager.entities.Salesman;
import com.miguelpazatto.leadsmanager.repositories.LeadRepository;
import com.miguelpazatto.leadsmanager.repositories.OptionRepository;

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
		return obj.map(LeadSalesDTO::new).orElseThrow();
	}
	
	public LeadPublicDTO publicFindById(Long id) {
		Optional<Lead> obj = repository.findById(id);
		return obj.map(LeadPublicDTO::new).orElseThrow();
	}
	
	public LeadSalesDTO insert(LeadRequestDTO data) {
		Salesman salesman = salesmanService.assignSalesman();
		Lead obj = new Lead(null, data.name(), data.email(), data.phone(), salesman);
		
		List<Answer> answers = new ArrayList<>();
		
		for (Long o : data.optionsId()) {
			Option option = optionRepository.findById(o).orElseThrow();
			answers.add(new Answer(option, obj));
		}
		
		obj.setOptions(answers);
		
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
		entity.setAssignedTo(obj.getAssignedTo());
	}
	
	public void markAsContacted(Long id) {
		Lead obj = repository.getReferenceById(id);
		obj.markAsContacted();	
	}
	
}
