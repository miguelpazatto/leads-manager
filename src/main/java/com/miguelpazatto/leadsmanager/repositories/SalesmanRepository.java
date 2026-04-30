package com.miguelpazatto.leadsmanager.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miguelpazatto.leadsmanager.entities.Salesman;

public interface SalesmanRepository extends JpaRepository<Salesman, Long> {

	public Optional<Salesman> findByEmail(String email);
	
	public Optional<Salesman> findFirstByOrderByLastLeadDateAsc();
	
}
