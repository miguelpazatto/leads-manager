package com.miguelpazatto.leadsmanager.dto;

import com.miguelpazatto.leadsmanager.entities.User;
import com.miguelpazatto.leadsmanager.entities.enums.UserRole;

public record UserResponseDTO(Long id, String login, UserRole role) {

	public UserResponseDTO(User user) {
		this( 
				user.getId(),
				user.getLogin(),
				user.getRole()
			);
	}
	
}
