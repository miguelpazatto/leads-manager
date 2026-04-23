package com.miguelpazatto.leadsmanager.dto;

import com.miguelpazatto.leadsmanager.entities.Option;

public record OptionDTO(String description) {

	public OptionDTO(Option option) {
		this (
				option.getDescription()
			);
		
	}
	
}
