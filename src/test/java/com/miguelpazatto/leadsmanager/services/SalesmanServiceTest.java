package com.miguelpazatto.leadsmanager.services;

import com.miguelpazatto.leadsmanager.dto.SalesmanDTO;
import com.miguelpazatto.leadsmanager.entities.Salesman;
import com.miguelpazatto.leadsmanager.entities.User;
import com.miguelpazatto.leadsmanager.entities.enums.UserRole;
import com.miguelpazatto.leadsmanager.repositories.SalesmanRepository;
import com.miguelpazatto.leadsmanager.services.exceptions.DatabaseException;
import com.miguelpazatto.leadsmanager.services.exceptions.ResourceNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalesmanServiceTest {

    @InjectMocks
    private SalesmanService salesmanService;

    @Mock
    private SalesmanRepository salesmanRepository;

    @Test
    @DisplayName("Deve retornar uma lista de Salesman quando ela não for vazia")
    void findAllSalesman_WhenListIsNotEmpty_ReturnListSalesmanDTO() {
        // given
        Salesman salesman = getSalesman();
        List<Salesman> salesmanList = List.of(salesman);

        given(salesmanRepository.findAll()).willReturn(salesmanList);

        // when
        List<SalesmanDTO> list = salesmanService.findAll();

        // then
        assertThat(list).hasSize(1);
        assertThat(list.getFirst().name()).isEqualTo(salesman.getName());

        verify(salesmanRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando não houver Salesman no bando de dados")
    void cannotFindAllSalesman_WhenListIsEmpty_ReturnEmptyList() {
        // given (sem dados no banco)

        given(salesmanRepository.findAll()).willReturn(List.of());

        // when
        List<SalesmanDTO> receivedSalesman = salesmanService.findAll();

        // then
        assertThat(receivedSalesman).isEmpty();

        verify(salesmanRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar um SalesmanDTO quando houver ID correspondente")
    void findSalesmanById_WhenIdExists_ReturnSalesmanDTO() {
        // given
        Salesman salesman = getSalesman();
        given(salesmanRepository.findById(salesman.getId())).willReturn(Optional.of(salesman));

        // when
        SalesmanDTO receivedSalesman = salesmanService.findById(salesman.getId());

        // then
        assertThat(receivedSalesman.id()).isEqualTo(salesman.getId());
        assertThat(receivedSalesman.name()).isEqualTo(salesman.getName());

        verify(salesmanRepository, times(1)).findById(salesman.getId());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando não houver ID correspondente")
    void cannotFindSalesmanById_WhenIdDoesNotExist_ThrowsResourceNotFoundException() {
        // given
        Long id = 999L;
        given(salesmanRepository.findById(id)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() ->  salesmanService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Resource not found. Id " + id);

        verify(salesmanRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve deletar Salesman quando houver ID correspondente")
    void deleteSalesman_WhenIdDoesExist_ReturnVoid() {
        // given
        Salesman salesman = getSalesman();
        given(salesmanRepository.existsById(salesman.getId())).willReturn(true);
        willDoNothing().given(salesmanRepository).deleteById(salesman.getId());

        // when
        salesmanService.delete(salesman.getId());

        // then
        verify(salesmanRepository, times(1)).existsById(salesman.getId());
        verify(salesmanRepository, times(1)).deleteById(salesman.getId());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando não houver ID correspondente")
    void cannotDeleteSalesman_WhenIdDoesNotExist_ThrowsResourceNotFoundException() {
        // given
        given(salesmanRepository.existsById(999L)).willReturn(false);

        // when
        // then
        assertThatThrownBy(() ->  salesmanService.delete(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Resource not found. Id " + 999);

        verify(salesmanRepository, times(1)).existsById(999L);
        verify(salesmanRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve lançar DatabaseException quando houer violação da integridade dos dados")
    void cannotDeleteSalesman_WhenHasDataIntegrityViolation_ThrowsDatabaseException() {
        // given
        Salesman salesman = getSalesman();
        DataIntegrityViolationException dataIntegrityViolationException = new DataIntegrityViolationException("Database error");

        given(salesmanRepository.existsById(salesman.getId())).willReturn(true);
        willThrow(dataIntegrityViolationException).given(salesmanRepository).deleteById(salesman.getId());

        // when
        // then
        assertThatThrownBy(() ->  salesmanService.delete(salesman.getId()))
                .isInstanceOf(DatabaseException.class)
                .hasMessageContaining(dataIntegrityViolationException.getMessage());

        verify(salesmanRepository, times(1)).existsById(salesman.getId());
        verify(salesmanRepository, times(1)).deleteById(salesman.getId());
    }

    @Test
    void update() {
    }

    @Test
    void assignSalesman() {
    }

    private static @NotNull Salesman getSalesman() {
        User user = new User(
                "login",
                "senha",
                UserRole.COLLABORATOR
        );

        Salesman salesman = new Salesman(
                1L,
                "Salesman",
                "salesman@Email.com",
                "11999999999",
                user
        );

        return salesman;
    }
}