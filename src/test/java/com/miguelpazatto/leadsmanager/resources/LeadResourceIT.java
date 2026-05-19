package com.miguelpazatto.leadsmanager.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miguelpazatto.leadsmanager.entities.*;
import com.miguelpazatto.leadsmanager.entities.enums.UserRole;
import com.miguelpazatto.leadsmanager.repositories.*;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Transactional
public class LeadResourceIT {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SalesmanRepository salesmanRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @BeforeEach
    public void setUp() {
        Lead lead = getLead();

        User user = new User(
                "login",
                "senha",
                UserRole.COLLABORATOR
        );
        userRepository.save(user);

        questionRepository.save(lead.getOptions().getFirst().getOption().getQuestion());

        Salesman salesman = lead.getAssignedTo();
        salesman.setUser(user);
        salesmanRepository.save(salesman);

        optionRepository.save(lead.getOptions().getFirst().getOption());
        optionRepository.save(lead.getOptions().getLast().getOption());

        leadRepository.save(lead);

        answerRepository.save(lead.getOptions().getFirst());
        answerRepository.save(lead.getOptions().getLast());
    }

    @Test
    @DisplayName("Deve retornar Status 200 (OK) e uma lista de Leads quando a lista não for vazia")
    void findAllLeads_WhenListIsNotEmpty_ReturnOk() throws Exception {

        mockMvc.perform(get("/leads")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

    private static @NonNull Lead getLead() {
        Salesman salesman = new Salesman();
        salesman.setName("Salesman");

        Lead lead = new Lead(null, "Lead", "lead@email.com", "11999999999", salesman);

        Question question = new Question(null, "Qual seu faturamento?");

        Option o1 = new Option(null, "Até 10K", 25, question);
        Option o2 = new Option(null, "Até 50K", 4, question);

        Answer a1 = new Answer(o1, lead);
        Answer a2 = new Answer(o2, lead);
        List<Answer> answers = List.of(a1, a2);

        lead.setOptions(answers);
        return lead;
    }

}
