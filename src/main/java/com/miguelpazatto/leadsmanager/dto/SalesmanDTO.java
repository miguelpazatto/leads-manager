package com.miguelpazatto.leadsmanager.dto;

import java.util.List;

import com.miguelpazatto.leadsmanager.entities.Salesman;
import com.miguelpazatto.leadsmanager.entities.enums.Role;

public record SalesmanDTO(Long id, String name, String email, String phone, Role role, List<LeadDTO> leads) {

	public SalesmanDTO(Salesman salesman) {
		this (
				salesman.getId(),
				salesman.getName(),
				salesman.getEmail(),
				salesman.getPhone(),
				salesman.getRole(),
				salesman.getLeads().stream().map(LeadDTO::new).toList()
			);
	}
	
}
