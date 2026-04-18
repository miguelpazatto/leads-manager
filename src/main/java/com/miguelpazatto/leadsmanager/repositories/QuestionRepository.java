package com.miguelpazatto.leadsmanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miguelpazatto.leadsmanager.entities.Question;

public interface QuestionRepository extends JpaRepository<Question, Long>{

}
