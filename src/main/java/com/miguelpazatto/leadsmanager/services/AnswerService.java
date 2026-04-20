package com.miguelpazatto.leadsmanager.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miguelpazatto.leadsmanager.entities.Answer;
import com.miguelpazatto.leadsmanager.repositories.AnswerRepository;

@Service
public class AnswerService {

	@Autowired
	private AnswerRepository repository;
	
	public List<Answer> findAll() {
		return repository.findAll();
	}
	
}
