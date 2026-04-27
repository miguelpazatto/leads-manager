package com.miguelpazatto.leadsmanager.dto;

import com.miguelpazatto.leadsmanager.entities.Option;

public record OptionIdDTO(Long id, String description) {

	public OptionIdDTO(Option option) {
		this (
				option.getId(),
				option.getDescription()
			);
	}
	
}
