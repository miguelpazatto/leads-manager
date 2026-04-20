package com.miguelpazatto.leadsmanager.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miguelpazatto.leadsmanager.entities.Answer;
import com.miguelpazatto.leadsmanager.services.AnswerService;

@RestController
@RequestMapping(value = "/answers")
public class AnswerResource {

	@Autowired
	private AnswerService service;
	
	@GetMapping
	public ResponseEntity<List<Answer>> findAll() {
		List<Answer> answers = service.findAll();
		return ResponseEntity.ok().body(answers);
	}
	
}
