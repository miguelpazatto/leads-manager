package com.miguelpazatto.leadsmanager.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miguelpazatto.leadsmanager.entities.Salesman;
import com.miguelpazatto.leadsmanager.repositories.SalesmanRepository;

@Service
public class SalesmanService {

	@Autowired
	private SalesmanRepository repository;
	
	public List<Salesman> findAll() {
		return repository.findAll();
	}
	
}
