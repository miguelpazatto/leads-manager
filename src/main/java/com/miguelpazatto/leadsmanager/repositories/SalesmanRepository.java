package com.miguelpazatto.leadsmanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miguelpazatto.leadsmanager.entities.Salesman;

public interface SalesmanRepository extends JpaRepository<Salesman, Long> {

}
