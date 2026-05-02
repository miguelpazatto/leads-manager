package com.miguelpazatto.leadsmanager.services;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.miguelpazatto.leadsmanager.dto.SalesmanDTO;
import com.miguelpazatto.leadsmanager.entities.Salesman;
import com.miguelpazatto.leadsmanager.repositories.SalesmanRepository;
import com.miguelpazatto.leadsmanager.services.exceptions.DatabaseException;
import com.miguelpazatto.leadsmanager.services.exceptions.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class SalesmanService {

	@Autowired
	private SalesmanRepository repository;
	
	public List<SalesmanDTO> findAll() {
		List<Salesman> salesman = repository.findAll();
		return salesman.stream().map(SalesmanDTO::new).toList();
	}
	
	public SalesmanDTO findById(Long id) {
		Optional<Salesman> obj = repository.findById(id);
		return obj.map(SalesmanDTO::new).orElseThrow(() -> new ResourceNotFoundException(id));
	}
	
	public SalesmanDTO insert(Salesman obj) {
		return new SalesmanDTO(repository.save(obj));
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
	
	public SalesmanDTO update(Long id, Salesman obj) {
		//config exception
		Salesman entity = repository.getReferenceById(id);
		updateData(entity, obj);
		return new SalesmanDTO (repository.save(entity));
	}
	
	private void updateData(Salesman entity, Salesman obj) {
		entity.setName(obj.getName());
		entity.setEmail(obj.getEmail());
		entity.setPhone(obj.getPhone());
	}
	
	@Transactional
	public Salesman assignSalesman() {
		Salesman salesman = repository.findFirstByOrderByLastLeadDateAsc().orElseThrow();
		salesman.setLastLeadDate(Instant.now());
		
		return salesman;
	}
	
}
