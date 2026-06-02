package com.miguelpazatto.leadsmanager.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miguelpazatto.leadsmanager.dto.OptionRequestDTO;
import com.miguelpazatto.leadsmanager.dto.OptionResponseDTO;
import com.miguelpazatto.leadsmanager.dto.OptionUpdateDTO;
import com.miguelpazatto.leadsmanager.entities.Option;
import com.miguelpazatto.leadsmanager.entities.Question;
import com.miguelpazatto.leadsmanager.repositories.OptionRepository;
import com.miguelpazatto.leadsmanager.repositories.QuestionRepository;
import jakarta.transaction.Transactional;
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

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class OptionResourceIT {

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private Option savedOption;

    private Question savedQuestion;

    @BeforeEach
    void setUp() {
        Question question = new Question(
                null,
                "Enunciado da questão"
        );
        savedQuestion = questionRepository.save(question);

        Option option = new Option(
                null,
                "Enunciado da opção",
                25,
                question
        );
        savedOption = optionRepository.save(option);
    }

    @Test
    @DisplayName("Deve retornar Status 200 (OK) e uma lista de Options")
    void findAll_WhenListIsNoEmpty_ReturnOk() throws Exception {

        mockMvc.perform(get("/options")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(savedOption.getId()))
                .andExpect(jsonPath("$[0].weight").value(savedOption.getWeight()));

    }

    @Test
    @DisplayName("Deve retornar Status 200 (OK) quando encontrar ID correspondente")
    void findById_WhenIdDoesExist_ReturnOk() throws Exception {
        Long optionId = savedOption.getId();

        mockMvc.perform(get("/options/{id}", optionId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedOption.getId()))
                .andExpect(jsonPath("$.description").value(savedOption.getDescription()));
    }

    @Test
    @DisplayName("Deve retornar Status 201 (Created) quando inserir uma nova Option")
    void insert_WhenDataIsValid_ReturnCreated() throws Exception {
        OptionRequestDTO newOption = new OptionRequestDTO(
                "Enunciado inserido",
                8,
                savedQuestion.getId()
        );

        mockMvc.perform(post("/options")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newOption)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("http://localhost/options/")))
                .andExpect(jsonPath("$.description").value("Enunciado inserido"))
                .andDo(print());
    }

    @Test
    @DisplayName("Deve retornar Status 204 (No Content) quando deletar uma Option")
    void delete_WhenIdDoesExist_ReturnNoContent() throws Exception {
        Long optionId = savedOption.getId();

        mockMvc.perform(delete("/options/{id}", optionId))
                .andExpect(status().isNoContent());

        boolean stillExists = optionRepository.existsById(optionId);
        assertFalse(stillExists, "Option não deletada do banco de dados");
    }

    @Test
    @DisplayName("Deve retornar Status 200 (OK) quando alterar uma Option")
    void update_WhenIdDoesExist_ReturnOk() throws Exception {
        OptionUpdateDTO toUpdateOption = new  OptionUpdateDTO(
                "Enunciado alterado",
                14
        );
        Long optionId = savedOption.getId();

        mockMvc.perform(put("/options/{id}", optionId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toUpdateOption)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Enunciado alterado"))
                .andExpect(jsonPath("$.id").value(optionId))
                .andDo(print());

    }
}