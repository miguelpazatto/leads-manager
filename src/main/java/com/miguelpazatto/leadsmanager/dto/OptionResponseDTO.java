package com.miguelpazatto.leadsmanager.dto;

import com.miguelpazatto.leadsmanager.entities.Option;

public record OptionResponseDTO(Long id, String description, Integer weight) {

	public OptionResponseDTO(Option option) {
		this (
				option.getId(),
				option.getDescription(),
				option.getWeight()
				);
	}
	
}
