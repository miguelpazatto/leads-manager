package com.miguelpazatto.leadsmanager.services;

import com.miguelpazatto.leadsmanager.dto.QuestionDTO;
import com.miguelpazatto.leadsmanager.dto.QuestionRequestDTO;
import com.miguelpazatto.leadsmanager.entities.Question;
import com.miguelpazatto.leadsmanager.repositories.QuestionRepository;
import com.miguelpazatto.leadsmanager.services.exceptions.DatabaseException;
import com.miguelpazatto.leadsmanager.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
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
    @DisplayName("Deve salvar uma question quando dados forem válidos")
    void insert_WhenDataIsValid_ReturnQuestionDTO() {
       QuestionRequestDTO questionRequestDTO = new QuestionRequestDTO(
               "Enunciado a ser incluido na questão nova"
       );

        Question question = new Question(
                1L,
                "Enunciado a ser incluido na questão nova"
        );

        given(questionRepository.save(any(Question.class))).willReturn(question);

        QuestionDTO questionDTO = questionService.insert(questionRequestDTO);

        ArgumentCaptor<Question> captor = ArgumentCaptor.forClass(Question.class);
        verify(questionRepository).save(captor.capture());
        Question capturedQuestion =  captor.getValue();

        assertThat(capturedQuestion.getId()).isNull();
        assertThat(capturedQuestion.getStatement()).isEqualTo(questionRequestDTO.statement());

        assertThat(questionDTO.id()).isEqualTo(question.getId());
        assertThat(questionDTO.statement()).isEqualTo(question.getStatement());
    }

    @Test
    @DisplayName("Deve deletar uma question quando houver ID correspondente")
    void deleteQuestion_WhenIdExist_ReturnVoid() {
        Long id = 1L;
        given(questionRepository.existsById(id)).willReturn(true);
        willDoNothing().given(questionRepository).deleteById(id);

        questionService.delete(id);

        verify(questionRepository, times(1)).existsById(id);
        verify(questionRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar deletar quando não houver ID correspondente")
    void cannotDeleteQuestion_WhenIdDoesNotExist_ThrowResourceNotFoundException() {
        Long id = 999L;
        given(questionRepository.existsById(id)).willReturn(false);

        assertThatThrownBy(() ->  questionService.delete(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Resource not found. Id " + id);

        verify(questionRepository, times(1)).existsById(id);
        verify(questionRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve lançar DatabaseException quando houver violação da integridade dos dados")
    void cannotDeleteQuestion_WhenDataViolated_ThrowDatabaseException() {
        Long id = 1L;

        DataIntegrityViolationException e = new DataIntegrityViolationException("Database error");

        given(questionRepository.existsById(id)).willReturn(true);
        willThrow(e).given(questionRepository).deleteById(id);

        assertThatThrownBy(() ->  questionService.delete(id))
                .isInstanceOf(DatabaseException.class)
                .hasMessageContaining("Database error");

        verify(questionRepository, times(1)).existsById(id);
        verify(questionRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Deve alterar uma question e retornar QuestionDTO quando houver ID correspondente")
    void updateQuestion_WhenIdExist_ReturnQuestionDTO() {
        Question question = new Question(
                1L,
                "Enunciado"
        );
        Long id = 1L;
        QuestionRequestDTO questionRequestDTO = new QuestionRequestDTO(
                "EnunciadoAlterado"
        );

        given(questionRepository.existsById(id)).willReturn(true);
        given(questionRepository.getReferenceById(id)).willReturn(question);
        given(questionRepository.save(question)).willReturn(question);

        QuestionDTO questionDTO = questionService.update(id, questionRequestDTO);

        assertThat(questionDTO.id()).isEqualTo(question.getId());
        assertThat(questionDTO.statement()).isEqualTo(questionRequestDTO.statement());

        verify(questionRepository, times(1)).existsById(id);
        verify(questionRepository, times(1)).getReferenceById(id);
        verify(questionRepository, times(1)).save(question);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar alterar quando não houver ID correspondente")
    void cannotUpdate_WhenIdDoesNotExist_ThrowResourceNotFoundException() {
        Long id = 999L;
        QuestionRequestDTO questionRequestDTO = new QuestionRequestDTO(
                "EnunciadoAlterado"
        );

        given(questionRepository.existsById(id)).willReturn(false);
        assertThatThrownBy(() -> questionService.update(id, questionRequestDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Resource not found. Id " + id);

        verify(questionRepository, times(1)).existsById(id);
        verify(questionRepository, never()).getReferenceById(anyLong());
        verify(questionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando não encontrar entidade no banco")
    void cannotUpdate_WhenEntityDoesNotExist_ThrowResourceNotFoundException() {
        Long id = 1L;
        QuestionRequestDTO questionRequestDTO = new QuestionRequestDTO(
                "EnunciadoAlterado"
        );

        given(questionRepository.existsById(id)).willReturn(true);
        willThrow(EntityNotFoundException.class).given(questionRepository).getReferenceById(id);

        assertThatThrownBy(() -> questionService.update(id, questionRequestDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Resource not found. Id " + id);

        verify(questionRepository, times(1)).existsById(id);
        verify(questionRepository, times(1)).getReferenceById(id);
        verify(questionRepository, never()).save(any());
    }
}