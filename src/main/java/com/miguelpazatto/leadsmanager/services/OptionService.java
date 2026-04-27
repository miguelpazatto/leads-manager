package com.miguelpazatto.leadsmanager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miguelpazatto.leadsmanager.dto.OptionIdDTO;
import com.miguelpazatto.leadsmanager.entities.Option;
import com.miguelpazatto.leadsmanager.repositories.OptionRepository;

@Service
public class OptionService {

	@Autowired
	private OptionRepository repository;
	
	public OptionIdDTO insert(Option obj) {
		return new OptionIdDTO(repository.save(obj));
	}
	
	public void delete(Long id) {
		//config exception
		repository.deleteById(id);
	}
	
}
