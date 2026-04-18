package com.miguelpazatto.leadsmanager.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.miguelpazatto.leadsmanager.entities.Lead;
import com.miguelpazatto.leadsmanager.entities.Question;
import com.miguelpazatto.leadsmanager.entities.Salesman;
import com.miguelpazatto.leadsmanager.entities.enums.LeadClassification;
import com.miguelpazatto.leadsmanager.entities.enums.LeadStatus;
import com.miguelpazatto.leadsmanager.repositories.LeadRepository;
import com.miguelpazatto.leadsmanager.repositories.QuestionRepository;
import com.miguelpazatto.leadsmanager.repositories.SalesmanRepository;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

	@Autowired
	private LeadRepository leadRepository;
	
	@Autowired
	private SalesmanRepository salesmanRepository;
	
	@Autowired
	private QuestionRepository questionRepository;
	
	public void run(String... args) throws Exception {
		
		Question q1 = new Question(null, "Você se sente indisposto logo ao acordar?");
		Question q2 = new Question(null, "Qual é o maior limitador da sua performance hoje?");
		
		questionRepository.saveAll(List.of(q1, q2));
		
		Salesman s1 = new Salesman(null, "Rodrigo", "rodrigo@email.com", "998456728");
		Salesman s2 = new Salesman(null, "Vinicius", "vinicius@email.com", "997654382");
		
		salesmanRepository.saveAll(List.of(s1, s2));
		
		Lead l1 = new Lead(null, "Miguel", "miguel@email.com", "994568812", 20, LeadStatus.NEW, LeadClassification.HOT, s1);
		Lead l2 = new Lead(null, "Igor", "igor@email.com", "993876431", 20, LeadStatus.CONTACTED, LeadClassification.WARM, s2);
		
		leadRepository.saveAll(List.of(l1, l2));
		
	}
	
}
