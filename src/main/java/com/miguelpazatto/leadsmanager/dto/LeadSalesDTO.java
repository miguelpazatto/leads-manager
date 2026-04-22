package com.miguelpazatto.leadsmanager.dto;

import com.miguelpazatto.leadsmanager.entities.Lead;
import com.miguelpazatto.leadsmanager.entities.enums.LeadStatus;

public record LeadSalesDTO(Long id, String name, String email, String phone, Integer totalScore, LeadStatus leadStatus, String title, String message, String assignedTo ) {

	public LeadSalesDTO(Lead lead) {
		this (
			lead.getId(),
			lead.getName(),
			lead.getEmail(),
			lead.getPhone(),
			lead.getTotalScore(),
			lead.getLeadStatus(),
			lead.getLeadClassification().getTitleSales(),
			lead.getLeadClassification().getMessageSales(),
			(lead.getAssignedTo() != null) ? lead.getAssignedTo().getName() : "Aguardando atribuição"
			);
	}
	
}
