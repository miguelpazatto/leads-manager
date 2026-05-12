package com.miguelpazatto.leadsmanager.services;

import com.miguelpazatto.leadsmanager.dto.LeadPublicDTO;
import com.miguelpazatto.leadsmanager.dto.LeadRequestDTO;
import com.miguelpazatto.leadsmanager.dto.LeadSalesDTO;
import com.miguelpazatto.leadsmanager.entities.*;
import com.miguelpazatto.leadsmanager.repositories.LeadRepository;
import com.miguelpazatto.leadsmanager.repositories.OptionRepository;
import com.miguelpazatto.leadsmanager.services.exceptions.ResourceNotFoundException;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LeadServiceTest {

    @InjectMocks
    private LeadService leadService;

    @Mock
    private LeadRepository leadRepository;

    @Mock
    private SalesmanService salesmanService;

    @Mock
    private OptionRepository optionRepository;

    @Test
    void findAllLeads_WhenListIsNotEmpty_ReturnListLeadSalesDTO() {
        // given
        Lead lead = getLead();
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
    void findLeadById_WhenIdExists_ReturnLeadSalesDTO() {
        // given
        Lead lead = getLead();
        Long id = 1L;

        given(leadRepository.findById(id)).willReturn(Optional.of(lead));

        // when
        LeadSalesDTO leadSalesDTO = leadService.findById(id);

        // then
        assertThat(leadSalesDTO.id()).isEqualTo(lead.getId());
        then(leadRepository).should().findById(id);
    }

    @Test
    void cannotFindLeadById_WhenIdDoesNotExist_ThrowsResourceNotFoundException() {
        // given
        Long id = 1L;

        given(leadRepository.findById(id)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> leadService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Resource not found. Id " + id);

        then(leadRepository).should().findById(id);

    }

    @Test
    void publicFindById_WhenIdExists_ReturnLeadPublicDTO() {
        // given
        Lead lead = getLead();
        Long id = 1L;

        given(leadRepository.findById(id)).willReturn(Optional.of(lead));

        // when
        LeadPublicDTO leadPublicDTO = leadService.publicFindById(id);

        // then
        assertThat(leadPublicDTO.totalScore()).isEqualTo(lead.getTotalScore());
        assertThat(leadPublicDTO.title()).isEqualTo(lead.getLeadClassification().getTitleLead());
        assertThat(leadPublicDTO.message()).isEqualTo(lead.getLeadClassification().getMessageLead());

        then(leadRepository).should().findById(id);
    }

    @Test
    void cannotPublicFindById_WhenIdDoesNotExists_ThrowsResourceNotFoundException() {
        // given
        Long id = 1L;
        given(leadRepository.findById(id)).willReturn(Optional.empty());

        // when
        //then
        assertThatThrownBy(() -> leadService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Resource not found. Id " + id);

        then(leadRepository).should().findById(id);
    }

    @Test
    void insertLead_WhenRequestHasOneOption_ReturnLeadSalesDTO() {
        Salesman salesman = new Salesman();

        Question question = new Question(1L, "Qual seu faturamento?");

        Option option = new Option(1L, "Até 10K", 25, question);
        List<Long> options = List.of(option.getId());

        LeadRequestDTO data = new LeadRequestDTO(
                "Lead",
                "lead@email.com",
                "16999999999",
                options
                );

        Lead savedLead = new Lead(
                1L,
                "Lead",
                "lead@email.com",
                "16999999999",
                salesman);

        Answer answer = new Answer(option, savedLead);
        savedLead.setOptions(List.of(answer));

        given(salesmanService.assignSalesman()).willReturn(salesman);
        given(optionRepository.findById(option.getId())).willReturn(Optional.of(option));
        given(leadRepository.save(any(Lead.class))).willReturn(savedLead);

        // when
        LeadSalesDTO leadSalesDTO = leadService.insert(data);

        // then
        assertThat(leadSalesDTO.id()).isEqualTo(savedLead.getId());

        ArgumentCaptor<Lead>  leadArgumentCaptor = ArgumentCaptor.forClass(Lead.class);
        verify(leadRepository).save(leadArgumentCaptor.capture());
        Lead capturedLead = leadArgumentCaptor.getValue();

        assertThat(capturedLead.getName()).isEqualTo(savedLead.getName());
        assertThat(capturedLead.getEmail()).isEqualTo(savedLead.getEmail());
        assertThat(capturedLead.getOptions()).hasSize(1);
        assertThat(capturedLead.getOptions().getFirst().getOption()).isEqualTo(option);

    }

    @Test
    void insertLead_WhenRequestHasTwoOptions_ReturnLeadSalesDTO() {
        Salesman salesman = new Salesman();

        Question question = new Question(1L, "Qual seu faturamento?");

        Option o1 = new Option(1L, "Até 10K", 25, question);
        Option o2 = new Option(2L, "Até 50K", 4, question);
        List<Long> options = List.of(o1.getId(), o2.getId());

        LeadRequestDTO data = new LeadRequestDTO(
                "Lead",
                "lead@email.com",
                "16999999999",
                options
        );

        Lead savedLead = new Lead(
                1L,
                "Lead",
                "lead@email.com",
                "16999999999",
                salesman
        );

        Answer a1 = new Answer(o1, savedLead);
        Answer a2 = new Answer(o2, savedLead);
        List<Answer> answers = List.of(a1, a2);

        savedLead.setOptions(answers);

        given(salesmanService.assignSalesman()).willReturn(salesman);
        given(optionRepository.findById(anyLong())).willReturn(Optional.of(o1), Optional.of(o2));
        given(leadRepository.save(any(Lead.class))).willReturn(savedLead);

        // when
        LeadSalesDTO leadSalesDTO = leadService.insert(data);

        // then
        assertThat(leadSalesDTO.id()).isEqualTo(savedLead.getId());

        ArgumentCaptor<Lead>  leadArgumentCaptor = ArgumentCaptor.forClass(Lead.class);
        verify(leadRepository).save(leadArgumentCaptor.capture());
        Lead capturedLead = leadArgumentCaptor.getValue();

        assertThat(capturedLead.getName()).isEqualTo(savedLead.getName());
        assertThat(capturedLead.getEmail()).isEqualTo(savedLead.getEmail());
        assertThat(capturedLead.getOptions()).hasSize(2);
        assertThat(capturedLead.getOptions().getFirst().getOption()).isEqualTo(o1);
        assertThat(capturedLead.getOptions().get(1).getOption()).isEqualTo(o2);
    }

    @Test
    void cannotInsertLead_WhenCantFindOptionsId_ThrowsResourceNotFoundException() {
        Salesman salesman = new Salesman();
        Long invalidId = 999L;
        List<Long> options = List.of(invalidId);

        LeadRequestDTO data = new LeadRequestDTO(
                "Lead",
                "lead@email.com",
                "16999999999",
                options
        );

        given(salesmanService.assignSalesman()).willReturn(salesman);
        given(optionRepository.findById(invalidId)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> leadService.insert(data))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Resource not found. Id " + invalidId);

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

    private static @NonNull Lead getLead() {
        Salesman salesman = new Salesman();
        salesman.setId(1L);
        salesman.setName("Salesman");

        Lead lead = new Lead(null, "Lead", "lead@email.com", "11999999999", salesman);

        Question question = new Question(1L, "Qual seu faturamento?");

        Option o1 = new Option(1L, "Até 10K", 25, question);
        Option o2 = new Option(2L, "Até 50K", 4, question);

        Answer a1 = new Answer(o1, lead);
        Answer a2 = new Answer(o2, lead);
        List<Answer> answers = List.of(a1, a2);

        lead.setOptions(answers);
        return lead;
    }
}