package com.miguelpazatto.leadsmanager.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miguelpazatto.leadsmanager.dto.LeadPublicDTO;
import com.miguelpazatto.leadsmanager.dto.LeadRequestDTO;
import com.miguelpazatto.leadsmanager.dto.LeadSalesDTO;
import com.miguelpazatto.leadsmanager.entities.*;
import com.miguelpazatto.leadsmanager.infra.security.TokenService;
import com.miguelpazatto.leadsmanager.repositories.UserRepository;
import com.miguelpazatto.leadsmanager.services.LeadService;
import com.miguelpazatto.leadsmanager.services.exceptions.ResourceNotFoundException;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(LeadResource.class)
@AutoConfigureMockMvc(addFilters = false)
class LeadResourceTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private LeadService leadService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    @DisplayName("Deve retornar Status 200 (OK) e uma lista de Leads quando a lista não for vazia")
    void findAll_WhenListIsNotEmpty_ReturnOk() throws Exception {
        // given
        Lead lead = getLead();
        LeadSalesDTO leadSalesDTO = new LeadSalesDTO(lead);
        List<LeadSalesDTO> leadSalesDTOS = List.of(leadSalesDTO);

        given(leadService.findAll()).willReturn(leadSalesDTOS);

        // when
        // then
        mockMvc.perform(get("/leads")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0]id").value(leadSalesDTOS.getFirst().id()))
                .andExpect(jsonPath("$.[0]name").value(leadSalesDTOS.getFirst().name()));
    }

    @Test
    @DisplayName("Deve retornar Status 200 (OK) e uma lista vazia quando não houver Leads nela")
    void cannotFindAll_WhenListIsEmpty_ReturnOk() throws Exception {
        // given
        given(leadService.findAll()).willReturn(List.of());

        //when
        //then
        mockMvc.perform(get("/leads")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

    }

    @Test
    @DisplayName("Deve retornar Status 200 (OK) e o Lead quando buscar por um ID existente")
    void findById_WhenIdExists_ReturnOk() throws Exception {
        // given
        Long id = 1L;
        Lead lead = getLead();
        LeadSalesDTO leadSalesDTO = new LeadSalesDTO(lead);

        given(leadService.findById(id)).willReturn(leadSalesDTO);

        // when
        // then
        mockMvc.perform(get("/leads/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(leadSalesDTO.id()))
                .andExpect(jsonPath("$.name").value(leadSalesDTO.name()));

    }

    @Test
    @DisplayName("Deve retornar Status 404 (Not Found) quando buscar por um ID inexistente")
    void cannotFindById_WhenIdDoesNotExist_ReturnNotFound() throws Exception {
        // given
        Long id = 999L;
        given(leadService.findById(id)).willThrow(ResourceNotFoundException.class);

        //when
        //then
        mockMvc.perform(get("/leads/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void publicFindById_WhenIdDoesExist_ReturnOk() throws Exception {
        // given
        Long id = 1L;
        Lead lead = getLead();
        LeadPublicDTO leadPublicDTO = new LeadPublicDTO(lead);

        given(leadService.publicFindById(id)).willReturn(leadPublicDTO);

        // when
        // then
        mockMvc.perform(get("/leads/public/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalScore").value(leadPublicDTO.totalScore()))
                .andExpect(jsonPath("$.title").value(leadPublicDTO.title()))
                .andExpect(jsonPath("$.message").value(leadPublicDTO.message()))
                .andDo(print());

    }

    @Test
    void cannotPublicFindById_WhenIdDoesNotExist_ReturnNotFound() throws Exception {
        // given
        Long id = 999L;
        given(leadService.publicFindById(id)).willThrow(ResourceNotFoundException.class);

        // when
        // then
        mockMvc.perform(get("/leads/public/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void insert() {
    }

    @Test
    void delete() {
    }

    @Test
    void update() {
    }

    @Test
    void markAsContacted() {
    }

    private static @NonNull Lead getLead() {
        Salesman salesman = new Salesman();
        salesman.setId(1L);
        salesman.setName("Salesman");

        Lead lead = new Lead(1L, "Lead", "lead@email.com", "11999999999", salesman);

        Question question = new Question(1L, "Qual seu faturamento?");

        Option o1 = new Option(1L, "Até 10K", 25, question);
        Option o2 = new Option(2L, "Até 50K", 4, question);

        Answer a1 = new Answer(o1, lead);
        Answer a2 = new Answer(o2, lead);
        List<Answer> answers = List.of(a1, a2);

        lead.setOptions(answers);
        return lead;
    }

}