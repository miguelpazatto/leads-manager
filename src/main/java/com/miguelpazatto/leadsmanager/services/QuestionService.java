package com.miguelpazatto.leadsmanager.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miguelpazatto.leadsmanager.dto.QuestionDTO;
import com.miguelpazatto.leadsmanager.entities.Question;
import com.miguelpazatto.leadsmanager.repositories.QuestionRepository;
import com.miguelpazatto.leadsmanager.services.exceptions.ResourceNotFoundException;

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
		//config exception
		repository.deleteById(id);
	}
	
	public QuestionDTO update(Long id, Question obj) {
		//config exception
		Question entity = repository.getReferenceById(id);
		updateData(entity, obj);
		return new QuestionDTO(repository.save(entity));
	}
	
	private void updateData(Question entity, Question obj) {
		entity.setStatement(obj.getStatement());
	}
}
