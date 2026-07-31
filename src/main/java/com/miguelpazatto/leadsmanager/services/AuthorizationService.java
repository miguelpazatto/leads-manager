package com.miguelpazatto.leadsmanager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.miguelpazatto.leadsmanager.dto.RegisterDTO;
import com.miguelpazatto.leadsmanager.dto.UserResponseDTO;
import com.miguelpazatto.leadsmanager.entities.Salesman;
import com.miguelpazatto.leadsmanager.entities.User;
import com.miguelpazatto.leadsmanager.entities.enums.UserRole;
import com.miguelpazatto.leadsmanager.repositories.SalesmanRepository;
import com.miguelpazatto.leadsmanager.repositories.UserRepository;
import com.miguelpazatto.leadsmanager.services.exceptions.BusinessException;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class 	AuthorizationService implements UserDetailsService {

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private SalesmanRepository salesmanRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return repository.findByLogin(username);
	}

	@Transactional
	public UserResponseDTO register(@Valid RegisterDTO data) {
		
		String encryptedPassword = passwordEncoder.encode(data.password());
		User newUser = new User(data.login(), encryptedPassword, data.role());
		
		repository.save(newUser);
		
		if (data.role() == UserRole.COLLABORATOR) {
			
			if (data.name() == null || data.email() == null || data.phone() == null) {
				throw new BusinessException("Dados de vendedor (nome, email, telefone) são obrigatórios para colaboradores.");
			}

			Salesman salesman = new Salesman();
			salesman.setUser(newUser);
			salesman.setName(data.name());
			salesman.setEmail(data.email());
			salesman.setPhone(data.phone());

			salesmanRepository.save(salesman);
			return new UserResponseDTO(salesman);
		}
		
		return new UserResponseDTO(newUser);
		
	}
	
}
