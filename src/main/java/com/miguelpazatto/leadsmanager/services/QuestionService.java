package com.miguelpazatto.leadsmanager.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miguelpazatto.leadsmanager.entities.Question;
import com.miguelpazatto.leadsmanager.repositories.QuestionRepository;

@Service
public class QuestionService {

	@Autowired
	private QuestionRepository repository;
	
	public List<Question> findAll() {
		return repository.findAll();
	}
	
	public Question findById(Long id) {
		Optional<Question> obj = repository.findById(id);
		return obj.orElseThrow();
	}
}
