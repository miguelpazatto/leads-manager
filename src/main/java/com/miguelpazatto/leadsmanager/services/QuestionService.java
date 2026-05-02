package com.miguelpazatto.leadsmanager.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.miguelpazatto.leadsmanager.dto.QuestionDTO;
import com.miguelpazatto.leadsmanager.entities.Question;
import com.miguelpazatto.leadsmanager.repositories.QuestionRepository;
import com.miguelpazatto.leadsmanager.services.exceptions.DatabaseException;
import com.miguelpazatto.leadsmanager.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class QuestionService {

	@Autowired
	private QuestionRepository repository;
	
	public List<QuestionDTO> findAll() {
		List<Question> questions = repository.findAll();
		return questions.stream().map(QuestionDTO::new).toList();
	}
	
	public QuestionDTO findById(Long id) {
		Optional<Question> obj = repository.findById(id);
		return obj.map(QuestionDTO::new).orElseThrow(() -> new ResourceNotFoundException(id));
	}
	
	public QuestionDTO insert(Question obj) {
		return new QuestionDTO(repository.save(obj));
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
	
	public QuestionDTO update(Long id, Question obj) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException(id);
		}
		try {
			Question entity = repository.getReferenceById(id);
			updateData(entity, obj);
			return new QuestionDTO(repository.save(entity));
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}
	
	private void updateData(Question entity, Question obj) {
		entity.setStatement(obj.getStatement());
	}
}
