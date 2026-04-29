package com.miguelpazatto.leadsmanager.dto;

import com.miguelpazatto.leadsmanager.entities.enums.UserRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterDTO(@NotBlank String login, @NotBlank String password, @NotNull UserRole role, String name, String email, String phone) {

}
