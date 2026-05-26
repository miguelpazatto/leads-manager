package com.miguelpazatto.leadsmanager.services;

import com.miguelpazatto.leadsmanager.dto.QuestionDTO;
import com.miguelpazatto.leadsmanager.entities.Question;
import com.miguelpazatto.leadsmanager.repositories.QuestionRepository;
import com.miguelpazatto.leadsmanager.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    @Test
    @DisplayName("Deve retornar uma lista de QuestionDTO quando ela não for vazia")
    void findAll_WhenListIsNotEmpty_ReturnListQuestionDTO() {
        Question question = new Question(
                1L,
                "Enunciado"
        );
        List<Question> questions = List.of(question);

        given(questionRepository.findAll()).willReturn(questions);

        List<QuestionDTO> questionDTOS = questionService.findAll();

        assertThat(questionDTOS).hasSize(1);
        assertThat(questionDTOS.getFirst().statement()).isEqualTo(question.getStatement());

        verify(questionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando não houver questões no banco")
    void findAll_WhenListIsEmpty_ReturnEmptyList() {
        given(questionRepository.findAll()).willReturn(List.of());

        List<QuestionDTO> questionsDTOS = questionService.findAll();

        assertThat(questionsDTOS).isEmpty();
        verify(questionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar um QuestionDTO quando houver ID correspondente")
    void findById_WhenIdDoesExist_ReturnQuestionDTO() {
        Question question = new Question(
                1L,
                "Enunciado"
        );
        Long id = 1L;

        given(questionRepository.findById(id)).willReturn(Optional.of(question));

        QuestionDTO questionDTO = questionService.findById(id);

        assertThat(questionDTO.id()).isEqualTo(question.getId());
        assertThat(questionDTO.statement()).isEqualTo(question.getStatement());

        verify(questionRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando não houver ID correspondente")
    void cannotFindById_WhenIdDoesNotExist_ThrowResourceNotFoundException() {
        Long id = 999L;
        given(questionRepository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() ->  questionService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Resource not found. Id " + id);

        verify(questionRepository, times(1)).findById(id);
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