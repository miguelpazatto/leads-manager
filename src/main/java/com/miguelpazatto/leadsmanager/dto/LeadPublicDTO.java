package com.miguelpazatto.leadsmanager.dto;

import com.miguelpazatto.leadsmanager.entities.Lead;

public record LeadPublicDTO(Integer totalScore, String title, String message) {

	public LeadPublicDTO(Lead lead) {
		this (
			lead.getTotalScore(),
			lead.getLeadClassification().getTitleLead(),
			lead.getLeadClassification().getMessageLead()
			);
		
	}
	
}
