package com.miguelpazatto.leadsmanager.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.miguelpazatto.leadsmanager.dto.OptionRequestDTO;
import com.miguelpazatto.leadsmanager.dto.OptionResponseDTO;
import com.miguelpazatto.leadsmanager.dto.OptionUpdateDTO;
import com.miguelpazatto.leadsmanager.entities.Option;
import com.miguelpazatto.leadsmanager.entities.Question;
import com.miguelpazatto.leadsmanager.repositories.OptionRepository;
import com.miguelpazatto.leadsmanager.repositories.QuestionRepository;
import com.miguelpazatto.leadsmanager.services.exceptions.DatabaseException;
import com.miguelpazatto.leadsmanager.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OptionService {

	@Autowired
	private OptionRepository repository;
	
	@Autowired QuestionRepository questionRepository;
	
	public List<OptionResponseDTO> findAll() {
		List<Option> options = repository.findAll();
		return options.stream().map(OptionResponseDTO::new).toList();
	}
	
	public OptionResponseDTO findById(Long id) {
		Optional<Option> option = repository.findById(id);
		return option.map(OptionResponseDTO::new).orElseThrow(() -> new ResourceNotFoundException(id));
	}
	
	public OptionResponseDTO insert(OptionRequestDTO dto) {
		Option option = new Option();
		option.setDescription(dto.description());
		option.setWeight(dto.weight());
		
		Question question = questionRepository.getReferenceById(dto.questionId());
		option.setQuestion(question);
		
		option = repository.save(option);
		
		return new OptionResponseDTO(option);
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
	
	public OptionResponseDTO update(Long id, OptionUpdateDTO obj) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException(id);
		}
		try {
			Option entity = repository.getReferenceById(id);
			updateData(entity, obj);
			return new OptionResponseDTO(repository.save(entity));
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}

	}
	
	private void updateData(Option entity, OptionUpdateDTO obj) {
		entity.setDescription(obj.description());
		entity.setWeight(obj.weight());
	}
}
