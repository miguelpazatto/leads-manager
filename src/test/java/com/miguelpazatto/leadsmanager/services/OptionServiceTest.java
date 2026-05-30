package com.miguelpazatto.leadsmanager.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miguelpazatto.leadsmanager.dto.OptionRequestDTO;
import com.miguelpazatto.leadsmanager.dto.OptionResponseDTO;
import com.miguelpazatto.leadsmanager.dto.OptionUpdateDTO;
import com.miguelpazatto.leadsmanager.entities.Option;
import com.miguelpazatto.leadsmanager.entities.Question;
import com.miguelpazatto.leadsmanager.repositories.OptionRepository;
import com.miguelpazatto.leadsmanager.repositories.QuestionRepository;
import com.miguelpazatto.leadsmanager.services.exceptions.DatabaseException;
import com.miguelpazatto.leadsmanager.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jdk.javadoc.doclet.Doclet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;


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
    @DisplayName("Deve retornar OptionResponseDTO quando inserir uma Option")
    void insertOption_WhenDataIsValid_ReturnOptionResponseDTO() {
        Long questionId = 1L;
        Question question = new Question(
                questionId,
                "Enunciado"
        );
        OptionRequestDTO dto = new OptionRequestDTO(
                "Enunciado inserido",
                8,
                questionId
        );
        Option option = new Option(
                1L,
                "Enunciado inserido",
                8,
                question
        );

        given(questionRepository.getReferenceById(questionId)).willReturn(question);
        given(optionRepository.save(any(Option.class))).willReturn(option);

        OptionResponseDTO optionResponseDTO = optionService.insert(dto);

        assertThat(optionResponseDTO.id()).isEqualTo(option.getId());
        assertThat(optionResponseDTO.description()).isEqualTo("Enunciado inserido");
        assertThat(optionResponseDTO.weight()).isEqualTo(8);

        ArgumentCaptor<Option> captor =  ArgumentCaptor.forClass(Option.class);
        verify(optionRepository).save(captor.capture());
        Option capturedOption = captor.getValue();

        assertThat(capturedOption.getDescription()).isEqualTo("Enunciado inserido");
        assertThat(capturedOption.getWeight()).isEqualTo(8);
        assertThat(capturedOption.getQuestion().getId()).isEqualTo(question.getId());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando tentar inserir Option para uma Question inexistente")
    void insertOption_WhenQuestionIdDoesNotExist_ThrowResourceNotFoundException() {

        Long invalidQuestionId = 999L;
        OptionRequestDTO dto = new OptionRequestDTO("Enunciado", 8, invalidQuestionId);

        given(questionRepository.getReferenceById(invalidQuestionId))
                .willThrow(new EntityNotFoundException());

        assertThatThrownBy(() -> optionService.insert(dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Resource not found");
    }

    @Test
    @DisplayName("Deve deletar uma Option quando houver ID correspondente")
    void deleteOption_WhenIdDoesExist_ReturnVoid() {

        Long optionId= 1L;
        given(optionRepository.existsById(optionId)).willReturn(true);

        optionService.delete(optionId);

        verify(optionRepository, times(1)).existsById(optionId);
        verify(optionRepository, times(1)).deleteById(optionId);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando não houver ID correspondente para deletar")
    void cannotDeleteOption_WhenIdDoesNotExist_ThrowResourceNotFoundException() {
        Long optionId = 999L;
        given(optionRepository.existsById(optionId)).willReturn(false);

        assertThatThrownBy(() -> optionService.delete(optionId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Resource not found");

        verify(optionRepository, times(1)).existsById(optionId);
        verify(optionRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve lançar DatabaseException quando houver violação da integridade do banco")
    void cannotDeleteOption_WhenDatabaseIntegrityViolation_ThrowDatabaseException() {

        Long id = 1L;
        DataIntegrityViolationException e = new DataIntegrityViolationException("Database error");

        given(optionRepository.existsById(id)).willReturn(true);
        willThrow(e).given(optionRepository).deleteById(id);

        assertThatThrownBy(() -> optionService.delete(id))
                .isInstanceOf(DatabaseException.class)
                .hasMessageContaining("Database error");

        verify(optionRepository, times(1)).existsById(id);
        verify(optionRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Deve alterar uma Option quando ID e Entidade existirem")
    void updateOption_WhenIdAndEntityExists_ReturnOptionResponseDTO() {
        Long optionId = 1L;
        OptionUpdateDTO dto = new OptionUpdateDTO("Enunciado alterado", 9);

        Question question = new Question(1L, "Enunciado da Questão");

        Option existingOption = new Option(optionId, "Enunciado antigo", 5, question);

        Option updatedOptionMock = new Option(optionId, "Enunciado alterado", 9, question);

        given(optionRepository.existsById(optionId)).willReturn(true);
        given(optionRepository.getReferenceById(optionId)).willReturn(existingOption);
        given(optionRepository.save(any(Option.class))).willReturn(updatedOptionMock);

        OptionResponseDTO responseDTO = optionService.update(optionId, dto);

        assertThat(responseDTO.id()).isEqualTo(optionId);
        assertThat(responseDTO.description()).isEqualTo(dto.description());
        assertThat(responseDTO.weight()).isEqualTo(dto.weight());

        verify(optionRepository, times(1)).existsById(optionId);
        verify(optionRepository, times(1)).getReferenceById(optionId);

        ArgumentCaptor<Option> captor = ArgumentCaptor.forClass(Option.class);
        verify(optionRepository, times(1)).save(captor.capture());

        Option capturedOption = captor.getValue();
        assertThat(capturedOption.getDescription()).isEqualTo(dto.description());
        assertThat(capturedOption.getWeight()).isEqualTo(dto.weight());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando não houver ID correspondente")
    void cannotUpdateOption_WhenIdDoesNotExist_ThrowResourceNotFoundException() {
        // given
        Long optionId = 999L;
        given(optionRepository.existsById(optionId)).willReturn(false);

        // when - then
        assertThatThrownBy(() -> optionService.update(optionId, new OptionUpdateDTO("Enunciado alterado", 9)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Resource not found");

        verify(optionRepository, times(1)).existsById(optionId);
        verify(optionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando não encontrar a entidade")
    void cannotUpdateOption_WhenEntityNotFound_ThrowResourceNotFoundException() {
        // given
        Long optionId = 1L;
        OptionUpdateDTO dto = new OptionUpdateDTO("Enunciado alterado", 8);

        given(optionRepository.existsById(optionId)).willReturn(true);
        given(optionRepository.getReferenceById(optionId)).willThrow(new EntityNotFoundException());

        // when - then
        assertThatThrownBy(() -> optionService.update(optionId, dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Resource not found");

        verify(optionRepository, times(1)).existsById(optionId);
        verify(optionRepository, times(1)).getReferenceById(optionId);
        verify(optionRepository, never()).save(any());
    }
}