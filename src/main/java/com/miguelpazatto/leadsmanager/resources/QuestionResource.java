package com.miguelpazatto.leadsmanager.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miguelpazatto.leadsmanager.dto.QuestionDTO;
import com.miguelpazatto.leadsmanager.entities.Question;
import com.miguelpazatto.leadsmanager.services.QuestionService;

@RestController
@RequestMapping(value = "/questions")
public class QuestionResource {

	@Autowired
	private QuestionService service;
	
	@GetMapping
	public ResponseEntity<List<QuestionDTO>> findAll() {
		List<QuestionDTO> questions = service.findAll();
		return ResponseEntity.ok().body(questions);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<QuestionDTO> findById(@PathVariable Long id) {
		QuestionDTO obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}
	
}
