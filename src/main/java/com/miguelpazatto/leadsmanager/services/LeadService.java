package com.miguelpazatto.leadsmanager.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miguelpazatto.leadsmanager.entities.Lead;
import com.miguelpazatto.leadsmanager.repositories.LeadRepository;

@Service
public class LeadService {

	@Autowired
	private LeadRepository repository;
	
	public List<Lead> findAll() {
		return repository.findAll();
	}
}
