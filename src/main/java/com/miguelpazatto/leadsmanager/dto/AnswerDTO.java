package com.miguelpazatto.leadsmanager.dto;

import com.miguelpazatto.leadsmanager.entities.Answer;

public record AnswerDTO(String quetionStatement, String optionStatement) {
	
	public AnswerDTO(Answer answer) {
		this (
			answer.getOption().getQuestion().getStatement(),
			answer.getOption().getDescription()
			);
	}
}
