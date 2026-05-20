package com.miguelpazatto.leadsmanager.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miguelpazatto.leadsmanager.dto.LeadRequestDTO;
import com.miguelpazatto.leadsmanager.dto.LeadUpdateDTO;
import com.miguelpazatto.leadsmanager.entities.*;
import com.miguelpazatto.leadsmanager.entities.enums.LeadStatus;
import com.miguelpazatto.leadsmanager.entities.enums.UserRole;
import com.miguelpazatto.leadsmanager.repositories.*;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Assertions;
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

import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    private Lead savedLead;

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

        savedLead = leadRepository.save(lead);
    }

    @Test
    @DisplayName("Deve retornar Status 200 (OK) e uma lista de Leads quando a lista não for vazia")
    void findAllLeads_WhenListIsNotEmpty_ReturnOk() throws Exception {

        mockMvc.perform(get("/leads")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(savedLead.getId()))
                .andExpect(jsonPath("$[0].name").value(savedLead.getName()))
                .andDo(print());
    }

    @Test
    @DisplayName("Deve retornar Status 200 (OK) quando a lista for vazia")
    void cannotFindAll_WhenListIsEmpty_ReturnOk() throws Exception {
        leadRepository.deleteAll();

        mockMvc.perform(get("/leads")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("Deve retornar Status 200 (OK) e um Lead quando buscar por ID existente")
    void findById_WhenIdExist_ReturnOk() throws Exception {

        mockMvc.perform(get("/leads/{id}", savedLead.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedLead.getId()))
                .andExpect(jsonPath("$.name").value(savedLead.getName()));
    }

    @Test
    @DisplayName("Deve retornar Status 404 (Not Found) quando buscar por um ID inexistente")
    void cannotFindById_WhenIdDoesNotExist_ReturnNotFound() throws Exception {

        mockMvc.perform(get("/leads/999")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource not found"));
    }

    @Test
    @DisplayName("Deve retornar Status 200 (OK) e um LeadPublicDTO quando buscar por ID existente")
    void publicFindById_WhenIdDoesExist_ReturnOk() throws Exception {

        mockMvc.perform(get("/leads/public/{id}", savedLead.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalScore").value(savedLead.getTotalScore()))
                .andExpect(jsonPath("$.title").value(savedLead.getLeadClassification().getTitleLead()))
                .andExpect(jsonPath("$.message").value(savedLead.getLeadClassification().getMessageLead()));
    }

    @Test
    @DisplayName("Deve retornar Status 404 (Not Found) quando buscar por ID inexistente")
    void cannotPublicFindById_WhenIdDoesNotExist_ReturnNotFound() throws Exception {

        mockMvc.perform(get("/leads/public/999")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource not found"));
    }

    @Test
    @DisplayName("Deve retornar Status 201 (Created) quando a entrada de dados for válida")
    void insertLead_WhenInputDataIsValid_ReturnIsCreated() throws Exception {
        Option o1 = savedLead.getOptions().getFirst().getOption();
        Option o2 = savedLead.getOptions().getLast().getOption();

        LeadRequestDTO leadRequestDTO = new LeadRequestDTO(
                "RequestedLead",
                "requestedlead@gmail.com",
                "11977777777",
                List.of(o1.getId(), o2.getId())

        );

        mockMvc.perform(post("/leads")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(leadRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(leadRequestDTO.name()))
                .andExpect(jsonPath("$.email").value(leadRequestDTO.email()))
                .andDo(print());

    }

    @Test
    @DisplayName("Deve retornar Status 400 (Bad Request) quando o DTO tiver dados inválidos")
    void cannotInsertLead_WhenRequestDataIsInvalid_ReturnBadRequest() throws Exception {
        LeadRequestDTO leadRequestDTO = new LeadRequestDTO(
                " ",
                "requestedlead.com",
                "119777",
                List.of()
        );

        mockMvc.perform(post("/leads")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(leadRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Erro de validação na entrada de dados"))
                .andExpect(jsonPath("$.errors[*].fieldName", hasItem("name")))
                .andExpect(jsonPath("$.errors[*].fieldName", hasItem("email")))
                .andExpect(jsonPath("$.errors[*].fieldName", hasItem("phone")))
                .andDo(print());
    }

    @Test
    @DisplayName("Deve retornar Status 204 (No Content) quando deletar um Lead por um ID existente")
    void delete_WhenIdExists_ReturnsNoContent() throws Exception {

        mockMvc.perform(delete("/leads/{id}", savedLead.getId()))
                .andExpect(status().isNoContent());

        boolean stillExists = leadRepository.existsById(savedLead.getId());
        assertFalse(stillExists, "Lead não deletado no bando de dados");
    }

    @Test
    @DisplayName("Deve retornar Status 404 (Not Found) quando tentar deletar um Lead com ID inexistente")
    void cannotDelete_WhenIdDoesNotExist_ReturnNotFound() throws Exception {

        mockMvc.perform(delete("/leads/999")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource not found"));
    }

    @Test
    @DisplayName("Deve retornar Status 200 (OK) e alterar um Lead quando a entrada de dados for válida")
    void updateLead_WhenLeadExists_ReturnsOk() throws Exception {

        LeadUpdateDTO leadUpdateDTO = new LeadUpdateDTO(
                "AlteredLead",
                "alteredlead@email.com",
                "21999999999"
        );

        mockMvc.perform(put("/leads/{id}", savedLead.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(leadUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(leadUpdateDTO.name()))
                .andExpect(jsonPath("$.email").value(leadUpdateDTO.email()))
                .andExpect(jsonPath("$.phone").value(leadUpdateDTO.phone()))
                .andDo(print());

        Lead alteredLead = leadRepository.findById(savedLead.getId()).get();
        Assertions.assertEquals(savedLead.getName(), alteredLead.getName());
    }

    @Test
    @DisplayName("Deve retornar Status 404 (Not Found) quando não houver ID correspondente para alterar Lead")
    void cannotUpdateLead_WhenIdDoesNotExist_ReturnNotFound() throws Exception {

        LeadUpdateDTO leadUpdateDTO = new LeadUpdateDTO(
                "AlteredLead",
                "alteredlead@email.com",
                "21999999999"
        );

        mockMvc.perform(put("/leads/999")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(leadUpdateDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource not found"));
    }

    @Test
    @DisplayName("Deve retornar Status 400 (Bad Request) ao tentar alterar Lead quando o DTO tiver dados inválidos")
    void cannotUpdateLead_WhenDTOIsNotValid_ReturnBadRequest() throws Exception {

        LeadUpdateDTO leadUpdateDTO = new LeadUpdateDTO(
                " ",
                "alteredlead",
                "2199"
        );

        mockMvc.perform(put("/leads/{id}", savedLead.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(leadUpdateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Erro de validação na entrada de dados"))
                .andExpect(jsonPath("$.errors[*].fieldName", hasItem("name")))
                .andExpect(jsonPath("$.errors[*].fieldName", hasItem("email")))
                .andExpect(jsonPath("$.errors[*].fieldName", hasItem("phone")))
                .andDo(print());
    }

    @Test
    @DisplayName("Deve retornar Status 204 (No Content) quando marcar Lead como contatado")
    void markLeadAsContacted_WhenLeadExists_ReturnNoContent() throws Exception {

        mockMvc.perform(put("/leads/{id}/contacted", savedLead.getId()))
                .andExpect(status().isNoContent());

        Lead contactedLead = leadRepository.findById(savedLead.getId()).get();
        assertEquals(contactedLead.getLeadStatus().name(), LeadStatus.CONTACTED.name());
    }

    @Test
    @DisplayName("Deve retornar Status 404 (Not Found) quando tentar marcar um Lead com ID inexistente como contatado")
    void cannotMarkLeadAsContacted_WhenIdDoesNotExist_ReturnNotFound() throws Exception {

        mockMvc.perform(put("/leads/999/contacted")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource not found"));
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
