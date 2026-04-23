package com.miguelpazatto.leadsmanager.dto;

import com.miguelpazatto.leadsmanager.entities.Lead;

public record LeadDTO(Long id, String name) {
	
	public LeadDTO(Lead lead) {
		this (
				lead.getId(),
				lead.getName()
			);
	}
}
