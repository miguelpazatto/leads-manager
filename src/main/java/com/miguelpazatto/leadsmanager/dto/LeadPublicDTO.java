package com.miguelpazatto.leadsmanager.dto;

import com.miguelpazatto.leadsmanager.entities.Lead;

public record LeadPublicDTO(String title, String message) {

	public LeadPublicDTO(Lead lead) {
		this (
			lead.getLeadClassification().getTitleLead(),
			lead.getLeadClassification().getMessageLead()
			);
		
	}
	
}
