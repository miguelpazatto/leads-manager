package com.miguelpazatto.leadsmanager.dto;

import java.util.List;

import com.miguelpazatto.leadsmanager.entities.Question;

public record QuestionDTO(String statement, List<OptionDTO> options) {

	public QuestionDTO(Question question) {
		this (
				question.getStatement(),
				question.getOptions().stream().map(OptionDTO::new).toList()
			);
	}
	
}
