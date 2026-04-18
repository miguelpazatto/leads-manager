package com.miguelpazatto.leadsmanager.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miguelpazatto.leadsmanager.entities.Option;
import com.miguelpazatto.leadsmanager.repositories.OptionRepository;

@Service
public class OptionService {

	@Autowired
	private OptionRepository repository;
	
	public List<Option> findAll() {
		return repository.findAll();
	}
	
}
