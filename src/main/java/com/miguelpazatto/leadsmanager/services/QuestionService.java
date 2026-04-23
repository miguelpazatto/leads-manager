package com.miguelpazatto.leadsmanager.services;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miguelpazatto.leadsmanager.dto.QuestionDTO;
import com.miguelpazatto.leadsmanager.entities.Question;
import com.miguelpazatto.leadsmanager.repositories.QuestionRepository;

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
		return obj.map(QuestionDTO::new).orElseThrow();
	}
}
