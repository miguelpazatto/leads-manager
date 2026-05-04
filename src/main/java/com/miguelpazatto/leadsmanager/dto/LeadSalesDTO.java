package com.miguelpazatto.leadsmanager.dto;

import java.util.List;

import com.miguelpazatto.leadsmanager.entities.Lead;
import com.miguelpazatto.leadsmanager.entities.enums.LeadStatus;

public record LeadSalesDTO(Long id, String name, String email, String phone, LeadStatus leadStatus, Integer totalScore, String title, String message, String assignedTo,
		List<AnswerDTO> answers) {

	public LeadSalesDTO(Lead lead) {
		this (
			lead.getId(),
			lead.getName(),
			lead.getEmail(),
			lead.getPhone(),
			lead.getLeadStatus(),
			lead.getTotalScore(),
			lead.getLeadClassification().getTitleSales(),
			lead.getLeadClassification().getMessageSales(),
			lead.getAssignedTo().getName(), 
			lead.getOptions().stream().map(AnswerDTO::new).toList()
			);
	}
	
}
