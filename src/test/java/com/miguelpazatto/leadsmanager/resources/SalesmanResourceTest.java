package com.miguelpazatto.leadsmanager.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miguelpazatto.leadsmanager.dto.SalesmanDTO;
import com.miguelpazatto.leadsmanager.dto.SalesmanUpdateDTO;
import com.miguelpazatto.leadsmanager.infra.security.TokenService;
import com.miguelpazatto.leadsmanager.repositories.UserRepository;
import com.miguelpazatto.leadsmanager.services.SalesmanService;
import com.miguelpazatto.leadsmanager.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SalesmanResource.class)
@AutoConfigureMockMvc(addFilters = false)
class SalesmanResourceTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private SalesmanService salesmanService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserRepository  userRepository;

    @Test
    @DisplayName("Deve retornar Statu 200 (OK) quando houver lista de salesmen")
    void findAllSalesman_WhenListIsNotEmpty_ReturnOk() throws Exception {
        // given
        SalesmanDTO salesmanDTO = new SalesmanDTO(
                1L,
                "Salesman",
                "salesman@email.com",
                "11999999999",
                List.of()
        );
        List<SalesmanDTO> list = List.of(salesmanDTO);

        given(salesmanService.findAll()).willReturn(list);

        // when
        // then
        mockMvc.perform(get("/salesman")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].id").value(1));

        verify(salesmanService, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar Status 200 (OK) quando a lista de salesmen for vazia")
    void findAllSalesman_WhenListIsEmpty_ReturnOk() throws Exception {
        given(salesmanService.findAll()).willReturn(List.of());

        mockMvc.perform(get("/salesman")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(salesmanService, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar Status 200 (OK) quando houver ID correspondente ao salesman")
    void findById_WhenIdExist_ReturnOk() throws Exception {
        SalesmanDTO salesmanDTO = new SalesmanDTO(
                1L,
                "Salesman",
                "salesman@email.com",
                "11999999999",
                List.of()
        );
        Long id = 1L;

        given(salesmanService.findById(id)).willReturn(salesmanDTO);

        mockMvc.perform(get("/salesman/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Salesman"));

        verify(salesmanService, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve retornar Status 404 (Not Found) quando não houver ID correspondente ao salesman")
    void cannotFindById_WhenIdDoesNotExist_ReturnNotFound() throws Exception {
        Long id = 1L;

        given(salesmanService.findById(id)).willThrow(ResourceNotFoundException.class);

        mockMvc.perform(get("/salesman/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource not found"));

        verify(salesmanService, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve retornar Status 204 (No Content) quando deletar um salesman")
    void delete_WhenIdExist_ReturnNoContent() throws Exception {
        Long id = 1L;
        willDoNothing().given(salesmanService).delete(id);

        mockMvc.perform(delete("/salesman/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(salesmanService, times(1)).delete(id);
    }

    @Test
    @DisplayName("Deve retornar Status 404 (Not Found) ao tentar deletar quando não houver ID correspondente")
    void cannotDelete_WhenIdDoesNotExist_ReturnNotFound() throws Exception {
        Long id = 999L;
        ResourceNotFoundException resourceNotFoundException = new ResourceNotFoundException("Resource not found. Id " + id);
        willThrow(resourceNotFoundException).given(salesmanService).delete(id);

        mockMvc.perform(delete("/salesman/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource not found"));

        verify(salesmanService, times(1)).delete(id);
    }

    @Test
    @DisplayName("Deve retornar Status 200 (OK) quando alterar salesman")
    void updateSalesman_WhenIdExist_ReturnOk() throws Exception {
        SalesmanUpdateDTO  salesmanUpdateDTO = new SalesmanUpdateDTO(
                "Updated Salesman",
                "updatedsalesman@email.com",
                "11888888888"
        );
        Long id = 1L;

        SalesmanDTO salesmanDTO = new SalesmanDTO(
                1L,
                salesmanUpdateDTO.name(),
                salesmanUpdateDTO.email(),
                salesmanUpdateDTO.phone(),
                List.of()
        );


        given(salesmanService.update(id, salesmanUpdateDTO)).willReturn(salesmanDTO);

        mockMvc.perform(put("/salesman/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(salesmanUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(salesmanUpdateDTO.name()))
                .andExpect(jsonPath("$.email").value(salesmanUpdateDTO.email()))
                .andExpect(jsonPath("$.phone").value(salesmanUpdateDTO.phone()));

        verify(salesmanService, times(1)).update(id, salesmanUpdateDTO);

    }

    @Test
    @DisplayName("Deve retornar Status 400 (Bad Request) quando entrada de dados for inválida")
    void cannotUpdateSalesman_WhenDTOIsInvalid_ReturnBadRequest() throws Exception {
        SalesmanUpdateDTO  salesmanUpdateDTO = new SalesmanUpdateDTO(
                " ",
                "updatedsalesman",
                "118"
        );
        Long id = 1L;

        mockMvc.perform(put("/salesman/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(salesmanUpdateDTO)))
                .andExpect(status().isBadRequest());

        verify(salesmanService, never()).update(id, salesmanUpdateDTO);
    }

    @Test
    @DisplayName("Deve retornar Status 404 (Not found) quando não existir ID correspondente para alterar")
    void cannotUpdateSalesman_WhenIdDoesNotExist_ReturnNotFound() throws Exception {
        Long id = 999L;
        SalesmanUpdateDTO  salesmanUpdateDTO = new SalesmanUpdateDTO(
                "Updated Salesman",
                "updatedsalesman@email.com",
                "11888888888"
        );

        given(salesmanService.update(id, salesmanUpdateDTO))
                .willThrow(new ResourceNotFoundException("Resource not found. Id " + id));

        mockMvc.perform(put("/salesman/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(salesmanUpdateDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource not found"));

        verify(salesmanService, times(1)).update(id, salesmanUpdateDTO);
    }
}