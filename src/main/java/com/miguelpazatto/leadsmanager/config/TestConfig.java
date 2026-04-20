package com.miguelpazatto.leadsmanager.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.miguelpazatto.leadsmanager.entities.Answer;
import com.miguelpazatto.leadsmanager.entities.Lead;
import com.miguelpazatto.leadsmanager.entities.Option;
import com.miguelpazatto.leadsmanager.entities.Question;
import com.miguelpazatto.leadsmanager.entities.Salesman;
import com.miguelpazatto.leadsmanager.entities.enums.LeadClassification;
import com.miguelpazatto.leadsmanager.entities.enums.LeadStatus;
import com.miguelpazatto.leadsmanager.repositories.AnswerRepository;
import com.miguelpazatto.leadsmanager.repositories.LeadRepository;
import com.miguelpazatto.leadsmanager.repositories.OptionRepository;
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
	
	@Autowired
	private OptionRepository optionRepository;
	
	@Autowired
	private AnswerRepository answerRepository;
	
	public void run(String... args) throws Exception {
		
		Question q1 = new Question(null, "Você se sente indisposto logo ao acordar?");
		Question q2 = new Question(null, "Qual é o maior limitador da sua performance hoje?");
		
		questionRepository.saveAll(List.of(q1, q2));
		
		Option o1 = new Option(null, "Sim, me sinto indisposto", 1, q1);
		Option o2 = new Option(null, "Depende muito do dia", 2, q1);
		Option o3 = new Option(null, "Me sinto muito disposto", 3, q1);
		Option o4 = new Option(null, "Estresse das reuniões do dia a dia", 1, q2);
		Option o5 = new Option(null, "Cansaço das viagens de negócios", 2, q2);
		Option o6 = new Option(null, "Não ter tempo pra cuidar de mim como PF", 3, q2);
		
		optionRepository.saveAll(List.of(o1, o2, o3, o4, o5, o6));
		
		Salesman s1 = new Salesman(null, "Rodrigo", "rodrigo@email.com", "998456728");
		Salesman s2 = new Salesman(null, "Vinicius", "vinicius@email.com", "997654382");
		
		salesmanRepository.saveAll(List.of(s1, s2));
		
		Lead l1 = new Lead(null, "Miguel", "miguel@email.com", "994568812", 20, LeadStatus.NEW, LeadClassification.HOT, s1);
		Lead l2 = new Lead(null, "Igor", "igor@email.com", "993876431", 20, LeadStatus.CONTACTED, LeadClassification.WARM, s2);
		
		leadRepository.saveAll(List.of(l1, l2));
		
		Answer a1 = new Answer(o6, l1);
		Answer a2 = new Answer(o3, l1);
		Answer a3 = new Answer(o4, l2);
		Answer a4 = new Answer(o5, l2);
		
		answerRepository.saveAll(List.of(a1, a2, a3, a4));
		
	}
	
}
