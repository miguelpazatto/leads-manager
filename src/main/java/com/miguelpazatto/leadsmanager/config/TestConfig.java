package com.miguelpazatto.leadsmanager.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.miguelpazatto.leadsmanager.entities.Lead;
import com.miguelpazatto.leadsmanager.entities.enums.LeadClassification;
import com.miguelpazatto.leadsmanager.entities.enums.LeadStatus;
import com.miguelpazatto.leadsmanager.repositories.LeadRepository;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

	@Autowired
	private LeadRepository leadRepository;
	
	public void run(String... args) throws Exception {
		
		Lead l1 = new Lead(null, "Miguel", "miguel@email.com", "994568812", 20, LeadStatus.NEW, LeadClassification.HOT);
		Lead l2 = new Lead(null, "Igor", "igor@email.com", "993876431", 20, LeadStatus.CONTACTED, LeadClassification.WARM);
		
		leadRepository.saveAll(Arrays.asList(l1, l2));
		
	}
	
}
