package com.miguelpazatto.leadsmanager.dto;

import com.miguelpazatto.leadsmanager.entities.Option;

public record OptionRequestDTO(String description, Integer weight) {

	public OptionRequestDTO(Option option) {
		this (
				option.getDescription(),
				option.getWeight()
				);
	}
	
}
