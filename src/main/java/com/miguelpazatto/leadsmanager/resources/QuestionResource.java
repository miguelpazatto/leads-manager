package com.miguelpazatto.leadsmanager.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
	
	@PostMapping
	public ResponseEntity<QuestionDTO> insert(@RequestBody Question obj) {
		QuestionDTO dto = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.id()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<QuestionDTO> update(@PathVariable Long id, @RequestBody Question obj) {
		QuestionDTO dto = service.update(id, obj);
		return ResponseEntity.ok().body(dto);
	}
	
}
