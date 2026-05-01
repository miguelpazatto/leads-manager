package com.miguelpazatto.leadsmanager.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miguelpazatto.leadsmanager.dto.OptionIdDTO;
import com.miguelpazatto.leadsmanager.dto.OptionRequestDTO;
import com.miguelpazatto.leadsmanager.entities.Option;
import com.miguelpazatto.leadsmanager.repositories.OptionRepository;

@Service
public class OptionService {

	@Autowired
	private OptionRepository repository;
	
	public List<OptionRequestDTO> findAll() {
		List<Option> options = repository.findAll();
		return options.stream().map(OptionRequestDTO::new).toList();
	}
	
	public OptionRequestDTO findById(Long id) {
		Optional<Option> option = repository.findById(id);
		return option.map(OptionRequestDTO::new).orElseThrow();
	}
	
	public OptionIdDTO insert(Option obj) {
		return new OptionIdDTO(repository.save(obj));
	}
	
	public void delete(Long id) {
		//config exception
		repository.deleteById(id);
	}
	
	public OptionRequestDTO update(Long id, Option obj) {
		Option entity = repository.getReferenceById(id);
		updateData(entity, obj);
		return new OptionRequestDTO(repository.save(entity));
	}
	
	private void updateData(Option entity, Option obj) {
		entity.setDescription(obj.getDescription());
		entity.setWeight(obj.getWeight());
	}
}
