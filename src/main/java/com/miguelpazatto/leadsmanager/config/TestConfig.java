package com.miguelpazatto.leadsmanager.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.miguelpazatto.leadsmanager.dto.RegisterDTO;
import com.miguelpazatto.leadsmanager.entities.Answer;
import com.miguelpazatto.leadsmanager.entities.Lead;
import com.miguelpazatto.leadsmanager.entities.Option;
import com.miguelpazatto.leadsmanager.entities.Question;
import com.miguelpazatto.leadsmanager.entities.Salesman;
import com.miguelpazatto.leadsmanager.entities.enums.LeadClassification;
import com.miguelpazatto.leadsmanager.entities.enums.LeadStatus;
import com.miguelpazatto.leadsmanager.entities.enums.UserRole;
import com.miguelpazatto.leadsmanager.repositories.AnswerRepository;
import com.miguelpazatto.leadsmanager.repositories.LeadRepository;
import com.miguelpazatto.leadsmanager.repositories.OptionRepository;
import com.miguelpazatto.leadsmanager.repositories.QuestionRepository;
import com.miguelpazatto.leadsmanager.repositories.SalesmanRepository;
import com.miguelpazatto.leadsmanager.services.AuthorizationService;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {
	
	@Autowired
	private LeadRepository leadRepository;
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private SalesmanRepository salesmanRepository;
	
	@Autowired
	private OptionRepository optionRepository;
	
	@Autowired
	private AnswerRepository answerRepository;
	
	@Autowired
	private AuthorizationService authService;
	
	
	public void run(String... args) throws Exception {
		
		authService.register(new RegisterDTO("miguelpazatto", "12345", UserRole.ADMIN, null, null, null));
		authService.register(new RegisterDTO("rodrigo", "123", UserRole.COLLABORATOR, "Rodrigo Santos", "rodrigo@email.com", "9999999999"));
		
		Salesman s1 = salesmanRepository.findByEmail("rodrigo@email.com").orElseThrow();
		
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
		
		Lead l1 = new Lead(null, "Miguel", "miguel@email.com", "994568812", LeadStatus.NEW, LeadClassification.HOT, s1);
		Lead l2 = new Lead(null, "Igor", "igor@email.com", "993876431", LeadStatus.CONTACTED, LeadClassification.WARM, s1);
		
		leadRepository.saveAll(List.of(l1, l2));
		
		Answer a1 = new Answer(o6, l1);
		Answer a2 = new Answer(o3, l1);
		Answer a3 = new Answer(o4, l2);
		Answer a4 = new Answer(o5, l2);
		
		answerRepository.saveAll(List.of(a1, a2, a3, a4));
		
	}
	
}
