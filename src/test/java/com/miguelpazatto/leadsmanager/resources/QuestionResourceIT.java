package com.miguelpazatto.leadsmanager.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miguelpazatto.leadsmanager.dto.QuestionDTO;
import com.miguelpazatto.leadsmanager.dto.QuestionRequestDTO;
import com.miguelpazatto.leadsmanager.entities.Question;
import com.miguelpazatto.leadsmanager.repositories.QuestionRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class QuestionResourceIT {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("Deve retornar Status 200 (OK) e uma lista de QuestionDTO")
    void findAllQuestions_WhenListIsNotEmpty_ReturnOk () throws Exception {
        Question question = new Question(
                null,
                "Enunciado"
        );
        questionRepository.save(question);

        mockMvc.perform(get("/questions")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].statement").value("Enunciado"));
    }

    @Test
    @DisplayName("Deve retornar Status 200 (OK) e um QuestionDTO quando houver ID correspondente")
    void findQuestionById_WhenIdDoesExist_ReturnOK() throws Exception {
        Question question = new Question(
                null,
                "Enunciado"
        );
        Question savedQuestion = questionRepository.save(question);

        mockMvc.perform(get("/questions/{id}", savedQuestion.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedQuestion.getId()))
                .andExpect(jsonPath("$.statement").value("Enunciado"));
    }

    @Test
    @DisplayName("Deve retornar Status 201 (Created) quando inserir uma Question")
    void insertQuestion_WhenRequestDataIsValid_ReturnCreated() throws Exception {
        QuestionRequestDTO questionRequestDTO = new QuestionRequestDTO(
                "Enunciado inserido"
        );

        mockMvc.perform(post("/questions")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(questionRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.statement").value("Enunciado inserido"));
    }

    @Test
    @DisplayName("Deve retornar Status 204 (No Content) quando deletar uma Question")
    void deleteQuestion_WhenIdExist_ReturnNoContent() throws Exception {
        Question question = new Question(
                null,
                "Enunciado"
        );
        Question savedQuestion = questionRepository.save(question);

        mockMvc.perform(delete("/questions/{id}", savedQuestion.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertThat(questionRepository.findById(savedQuestion.getId())).isNotPresent();
    }

    @Test
    @DisplayName("Deve retornar Status 200 (OK) quando alterar uma questão com ID existente")
    void updateQuestion_WhenIdDoesExist_ReturnOk() throws Exception {
        Question question = new Question(
                null,
                "Enunciado"
        );
        Question savedQuestion = questionRepository.save(question);
        QuestionRequestDTO questionRequestDTO = new QuestionRequestDTO(
                "Enunciado alterado"
        );

        mockMvc.perform(put("/questions/{id}", savedQuestion.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(questionRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedQuestion.getId()))
                .andExpect(jsonPath("$.statement").value("Enunciado alterado"));

        Question alteredQuestion = questionRepository.findById(savedQuestion.getId()).get();
        assertThat(alteredQuestion.getStatement()).isEqualTo("Enunciado alterado");

    }
}