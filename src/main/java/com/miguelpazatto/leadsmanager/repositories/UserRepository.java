package com.miguelpazatto.leadsmanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.miguelpazatto.leadsmanager.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{

	public UserDetails findByLogin(String login);
	
}
