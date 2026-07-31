package com.miguelpazatto.leadsmanager.dto;

import com.miguelpazatto.leadsmanager.entities.Salesman;
import com.miguelpazatto.leadsmanager.entities.User;
import com.miguelpazatto.leadsmanager.entities.enums.UserRole;

public record UserResponseDTO(Long id, Long userId, String login, UserRole role) {

	public UserResponseDTO(Salesman salesman) {
		this(
				salesman.getId(),
				salesman.getUser().getId(),
				salesman.getUser().getLogin(),
				salesman.getUser().getRole()
		);
	}

	public UserResponseDTO(User user) {
		this(
				null,
				user.getId(),
				user.getLogin(),
				user.getRole()
		);
	}

}
