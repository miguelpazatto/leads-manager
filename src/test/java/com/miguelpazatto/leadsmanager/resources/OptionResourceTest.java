package com.miguelpazatto.leadsmanager.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miguelpazatto.leadsmanager.dto.OptionRequestDTO;
import com.miguelpazatto.leadsmanager.dto.OptionResponseDTO;
import com.miguelpazatto.leadsmanager.entities.Option;
import com.miguelpazatto.leadsmanager.entities.Question;
import com.miguelpazatto.leadsmanager.infra.security.TokenService;
import com.miguelpazatto.leadsmanager.repositories.UserRepository;
import com.miguelpazatto.leadsmanager.services.OptionService;
import com.miguelpazatto.leadsmanager.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OptionResource.class)
@AutoConfigureMockMvc(addFilters = false)
class OptionResourceTest {

   @Autowired
   private MockMvc mockMvc;

   ObjectMapper objectMapper = new ObjectMapper();

   @MockitoBean
   private OptionService optionService;

   @MockitoBean
   private TokenService tokenService;

   @MockitoBean
   private UserRepository userRepository;

    @Test
    @DisplayName("Deve retornar Status 200 (OK) e mostrar todas as Options quando lista não for vazia")
    void findAll_WhenListIsNotEmpty_ReturnOK() throws Exception {
        // given
        OptionResponseDTO optionResponseDTO = new OptionResponseDTO(
                1L,
                "Enunciado da opção",
                25
        );
        List<OptionResponseDTO> optionList = List.of(optionResponseDTO);

        given(optionService.findAll()).willReturn(optionList);

        // when - then
        mockMvc.perform(get("/options")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(optionResponseDTO.id()));

        verify(optionService, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar Status 200 (OK) e uma lista vazia quando não houver options")
    void findAll_WhenListIsEmpty_ReturnOk() throws Exception {
        // given
        List<OptionResponseDTO> optionList = List.of();
        given(optionService.findAll()).willReturn(optionList);

        // when - then
        mockMvc.perform(get("/options")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(optionService, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar Status 200 (OK) e uma Option quando houver ID correspondente")
    void findById_WhenIdDoesExist_ReturnOk() throws Exception {
        // given
        OptionResponseDTO optionResponseDTO = new OptionResponseDTO(
                1L,
                "Enunciado da opção",
                25
        );
        Long optionId = 1L;

        given(optionService.findById(optionId)).willReturn(optionResponseDTO);

        // when - then
        mockMvc.perform(get("/options/{id}", optionId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(optionResponseDTO.id()))
                .andExpect(jsonPath("$.description").value(optionResponseDTO.description()));

        verify(optionService, times(1)).findById(optionId);
    }

    @Test
    @DisplayName("Deve retornar Status 404 (Not Found) quando o ID não existir")
    void findById_WhenIdDoesNotExist_ReturnNotFound() throws Exception {
        // given
        Long optionId = 999L;
        given(optionService.findById(optionId)).willThrow(new ResourceNotFoundException("Resource not found"));

        // when - then
        mockMvc.perform(get("/options/{id}", optionId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource not found"));

        verify(optionService, times(1)).findById(optionId);
    }

    @Test
    @DisplayName("Deve retornar Status 201 (Created) e inserir uma Option quando dados forem válidos")
    void insert_WhenDataIsValid_ReturnCreated() throws Exception {
        Question question = new Question(1L, "Enunciado da questão");
        OptionRequestDTO newOption = new OptionRequestDTO(
                "Enunciado da opção",
                12,
                1L
        );
        OptionResponseDTO insertedOption = new OptionResponseDTO(
                1L,
                "Enunciado da opção",
                12
        );

        given(optionService.insert(newOption)).willReturn(insertedOption);

        // when - then
        mockMvc.perform(post("/options")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newOption)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(insertedOption.id()))
                .andExpect(jsonPath("$.description").value(insertedOption.description()));

        verify(optionService, times(1)).insert(newOption);
    }

    @Test
    @DisplayName("Deve retornar Status 400 (Bad Request) quando dados de entrada forem inválidos")
    void insert_WhenDataIsInvalid_ReturnBadRequest() throws Exception {
        // given
        OptionRequestDTO newOption = new OptionRequestDTO(
                "  ",
                1,
                1L
        );

        // when - then
        mockMvc.perform(post("/options")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newOption)))
                .andExpect(status().isBadRequest());

        verify(optionService, never()).insert(newOption);
    }

    @Test
    @DisplayName("Deve retornar Status 204 (No Content) quando deletar uma Option")
    void delete_WhenIdDoesExist_ReturnNoContent() throws Exception {
        // given
        Long optionId = 1L;
        willDoNothing().given(optionService).delete(optionId);

        // when - then
        mockMvc.perform(delete("/options/{id}", optionId))
                .andExpect(status().isNoContent());

        verify(optionService, times(1)).delete(optionId);
    }

    @Test
    @DisplayName("Deve retornar 404 (Not Found) quando não houver ID correspondente para deletar")
    void delete_WhenIdDoesNotExist_ReturnNotFound() throws Exception {
        // given
        Long optionId = 999L;
        willThrow(new ResourceNotFoundException("Resource not found")).given(optionService).delete(optionId);

        mockMvc.perform(delete("/options/{id}", optionId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource not found"));

        verify(optionService, times(1)).delete(optionId);
    }

    @Test
    void update() {
    }
}