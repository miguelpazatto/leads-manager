package com.miguelpazatto.leadsmanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miguelpazatto.leadsmanager.entities.Answer;
import com.miguelpazatto.leadsmanager.entities.pk.AnswerPK;

public interface AnswerRepository extends JpaRepository<Answer, AnswerPK> {

}
