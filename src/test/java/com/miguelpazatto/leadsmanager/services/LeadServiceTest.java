package com.miguelpazatto.leadsmanager.services;

import com.miguelpazatto.leadsmanager.dto.LeadSalesDTO;
import com.miguelpazatto.leadsmanager.entities.*;
import com.miguelpazatto.leadsmanager.repositories.LeadRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class LeadServiceTest {

    @Mock
    private LeadRepository leadRepository;

    @InjectMocks
    private LeadService leadService;

    @Test
    void findAllLeads_WhenListIsNotEmpty_ReturnListLeadSalesDTO() {
        // given
        Salesman salesman = new Salesman();
        salesman.setId(1L);
        salesman.setName("Salesman");

        Lead lead = new Lead(null, "Lead", "lead@email.com", "11999999999", salesman);

        Question question = new Question(1L, "Qual seu faturamento?");

        Option o1 = new Option(1L, "Até 10K", 25, question);
        Option o2 = new Option(1L, "Até 50K", 4, question);

        Answer a1 = new Answer(o1, lead);
        Answer a2 = new Answer(o2, lead);
        List<Answer> answers = List.of(a1, a2);

        lead.setOptions(answers);

        List<Lead> leads = List.of(lead);
        given(leadRepository.findAll()).willReturn(leads);

        // when
        List<LeadSalesDTO> list = leadService.findAll();

        // then
        assertThat(list).isNotEmpty();
        assertThat(list).hasSize(1);
        assertThat(list.getFirst().name()).isEqualTo("Lead");

        then(leadRepository).should().findAll();

    }

    @Test
    void findAllLeads_WhenListIsEmpty_ReturnEmptyList() {
        // given
        given(leadRepository.findAll()).willReturn(List.of());

        // when
        List<LeadSalesDTO> list = leadService.findAll();

        // then
        assertThat(list).isEmpty();
        then(leadRepository).should().findAll();
    }

    @Test
    void findById() {
    }

    @Test
    void publicFindById() {
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
}