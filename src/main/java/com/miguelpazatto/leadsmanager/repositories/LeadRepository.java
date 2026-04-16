package com.miguelpazatto.leadsmanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miguelpazatto.leadsmanager.entities.Lead;

public interface LeadRepository extends JpaRepository<Lead, Long> {

}
