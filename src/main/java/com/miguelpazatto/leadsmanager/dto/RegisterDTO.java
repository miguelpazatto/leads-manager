package com.miguelpazatto.leadsmanager.dto;

import com.miguelpazatto.leadsmanager.entities.enums.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {

}
