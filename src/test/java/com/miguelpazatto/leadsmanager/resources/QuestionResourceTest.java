package com.miguelpazatto.leadsmanager.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miguelpazatto.leadsmanager.dto.QuestionDTO;
import com.miguelpazatto.leadsmanager.dto.QuestionRequestDTO;
import com.miguelpazatto.leadsmanager.entities.Question;
import com.miguelpazatto.leadsmanager.infra.security.TokenService;
import com.miguelpazatto.leadsmanager.repositories.UserRepository;
import com.miguelpazatto.leadsmanager.services.QuestionService;
import com.miguelpazatto.leadsmanager.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuestionResource.class)
@AutoConfigureMockMvc(addFilters = false)
class QuestionResourceTest {

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private QuestionService questionService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private TokenService tokenService;

    @Test
    @DisplayName("Deve retornar Status 200 (OK) e uma lista de QuestionDTO")
    void findAllQuestions_WhenListIsNotEmpty_ReturnOk () throws Exception {
        QuestionDTO questionDTO = new QuestionDTO(
                1L,
                "Enunciado",
                List.of()
        );
        List<QuestionDTO> questionDTOs = List.of(questionDTO);
        given(questionService.findAll()).willReturn(questionDTOs);

        mockMvc.perform(get("/questions")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(questionService, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar Status 200 (OK) quando a lista for vazia")
    void findAllQuestion_WhenListIsEmpty_ReturnOk () throws Exception {
        List<QuestionDTO> questionDTOs = List.of();
        given(questionService.findAll()).willReturn(questionDTOs);

        mockMvc.perform(get("/questions")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(questionService, times(1)).findAll();
    }


    @Test
    @DisplayName("Deve retornar Status 200 (OK) e um QuestionDTO quando houver ID correspondente")
    void findQuestionById_WhenIdDoesExist_ReturnOK() throws Exception {
        QuestionDTO questionDTO = new QuestionDTO(
                1L,
                "Enunciado",
                List.of()
        );
        Long id = 1L;
        given(questionService.findById(id)).willReturn(questionDTO);

        mockMvc.perform(get("/questions/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(questionDTO.id()))
                .andExpect(jsonPath("$.statement").value(questionDTO.statement()));

        verify(questionService, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve retornar Status 404 (Not Found) quando não houver ID correspondente")
    void findQuestionById_WhenIdDoesNotExist_ReturnNotFound() throws Exception {
        Long id = 999L;
        given(questionService.findById(id)).willThrow(new ResourceNotFoundException(id));

        mockMvc.perform(get("/questions/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource not found"));

        verify(questionService, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve retornar Status 201 (Created) quando inserir uma Question")
    void insertQuestion_WhenRequestDataIsValid_ReturnCreated() throws Exception {
        QuestionRequestDTO questionRequestDTO = new QuestionRequestDTO(
                "Enunciado inserido"
        );
        QuestionDTO questionDTO = new QuestionDTO(
                1L,
                "Enunciado inserido",
                List.of()
        );

        given(questionService.insert(questionRequestDTO)).willReturn(questionDTO);

        mockMvc.perform(post("/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(questionRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/questions/1"))
                .andExpect(jsonPath("$.id").value(questionDTO.id()))
                .andExpect(jsonPath("$.statement").value(questionDTO.statement()));

        verify(questionService, times(1)).insert(questionRequestDTO);
    }

    @Test
    @DisplayName("Deve retornar Status 400 (Bad Request) quando tentar inserir uma Question com dados inválidos")
    void cannotInsertQuestion_WhenRequestDataIsInvalid_ReturnBadRequest() throws Exception {

        QuestionRequestDTO questionRequestDTO = new QuestionRequestDTO(
                ""
        );

        mockMvc.perform(post("/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(questionRequestDTO)))
                .andExpect(status().isBadRequest());

        verify(questionService, never()).insert(any());

    }

    @Test
    @DisplayName("Deve retornar Status 204 (No Content) quando deletar uma Question")
    void deleteQuestion_WhenIdExist_ReturnNoContent() throws Exception {
        Long id = 1L;

        willDoNothing().given(questionService).delete(id);

        mockMvc.perform(delete("/questions/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(questionService, times(1)).delete(id);

    }

    @Test
    @DisplayName("Deve retornar 404 (Not Found) quando tentar deletar uma Question sem ID no banco")
    void cannotDeleteQuestion_WhenIdDoesNotExist_ReturnNotFound() throws Exception {
        Long id = 999L;
        willThrow(new ResourceNotFoundException(id)).given(questionService).delete(id);

        mockMvc.perform(delete("/questions/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource not found"));

        verify(questionService, times(1)).delete(id);
    }

    @Test
    @DisplayName("Deve retornar Status 200 (OK) quando alterar uma questão com ID existente")
    void updateQuestion_WhenIdDoesExist_ReturnOk() throws Exception {

        Long id = 1L;
        QuestionRequestDTO questionRequestDTO = new QuestionRequestDTO(
                "Enunciado alterado"
        );
        QuestionDTO questionDTO = new QuestionDTO(
                1L,
                "Enunciado alterado",
                List.of()
        );

        given(questionService.update(id, questionRequestDTO)).willReturn(questionDTO);

        mockMvc.perform(put("/questions/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(questionRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(questionDTO.id()))
                .andExpect(jsonPath("$.statement").value(questionDTO.statement()));

        verify(questionService, times(1)).update(id, questionRequestDTO);
    }

    @Test
    @DisplayName("Deve lançar Status 404 (Not Found) quando não tiver ID correspondente para alterar")
    void cannotUpdateQuestion_WhenIdDoesNotExist_ReturnNotFound() throws Exception {

        Long id = 999L;
        QuestionRequestDTO questionRequestDTO = new QuestionRequestDTO(

                "Enunciado alterado"

        );

        given(questionService.update(id, questionRequestDTO)).willThrow(new ResourceNotFoundException(id));

        mockMvc.perform(put("/questions/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(questionRequestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource not found"));

        verify(questionService, times(1)).update(id, questionRequestDTO);
    }
}