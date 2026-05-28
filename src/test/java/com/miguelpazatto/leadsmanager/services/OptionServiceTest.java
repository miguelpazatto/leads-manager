package com.miguelpazatto.leadsmanager.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miguelpazatto.leadsmanager.dto.OptionResponseDTO;
import com.miguelpazatto.leadsmanager.entities.Option;
import com.miguelpazatto.leadsmanager.entities.Question;
import com.miguelpazatto.leadsmanager.repositories.OptionRepository;
import com.miguelpazatto.leadsmanager.repositories.QuestionRepository;
import com.miguelpazatto.leadsmanager.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class OptionServiceTest {

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private OptionService optionService;

    @Test
    @DisplayName("Deve retornar uma lista de Options quando não for vazia")
    void findAllOptions_WhenListIsNotEmpty_ReturnListOptionResponseDTO() {
        Question question = new Question(
                1L,
                "Enunciado"
        );
        Option option = new Option(
                1L,
                "Opção aleatória",
                25,
                question
        );
        List<Option> options = List.of(option);

        given(optionRepository.findAll()).willReturn(options);

        List<OptionResponseDTO> responseDTOs = optionService.findAll();

        assertThat(responseDTOs).hasSize(1);
        assertThat(responseDTOs.getFirst().id()).isEqualTo(option.getId());
        assertThat(responseDTOs.getFirst().description()).isEqualTo(option.getDescription());
        assertThat(responseDTOs.getFirst().weight()).isEqualTo(option.getWeight());

        verify(optionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando não houver Options salvas")
    void findAllOptions_WhenListIsEmpty_ReturnEmptyList() {
        List<Option> options = List.of();
        given(optionRepository.findAll()).willReturn(options);

        List<OptionResponseDTO> responseDTOs = optionService.findAll();

        assertThat(responseDTOs).isEmpty();
        verify(optionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar um OptionResponseDTO quando houver ID correspondente")
    void findOptionById_WhenIdDoesExist_ReturnOptionResponseDTO() {
        Question question = new Question(
                1L,
                "Enunciado"
        );
        Option option = new Option(
                1L,
                "Opção aleatória",
                25,
                question
        );

        Long id = 1L;
        given(optionRepository.findById(id)).willReturn(Optional.of(option));

        OptionResponseDTO optionResponseDTO = optionService.findById(id);

        assertThat(optionResponseDTO.id()).isEqualTo(option.getId());
        assertThat(optionResponseDTO.description()).isEqualTo(option.getDescription());
        assertThat(optionResponseDTO.weight()).isEqualTo(option.getWeight());

        verify(optionRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando não houver ID correspondente")
    void cannotFindOptionById_WhenIdDoesNotExist_ThrowResourceNotFoundException() {
        Long id = 999L;
        given(optionRepository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() ->  optionService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Resource not found");

        verify(optionRepository, times(1)).findById(id);
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
}