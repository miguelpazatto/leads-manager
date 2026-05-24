package com.miguelpazatto.leadsmanager.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miguelpazatto.leadsmanager.dto.SalesmanDTO;
import com.miguelpazatto.leadsmanager.infra.security.TokenService;
import com.miguelpazatto.leadsmanager.repositories.UserRepository;
import com.miguelpazatto.leadsmanager.services.SalesmanService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SalesmanResource.class)
@AutoConfigureMockMvc(addFilters = false)
class SalesmanResourceTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private SalesmanService salesmanService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository  userRepository;

    @Test
    @DisplayName("Deve retornar uma ista de Salesman quando ela não for vazia")
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
    }

    @Test
    void findById() {
    }

    @Test
    void delete() {
    }

    @Test
    void update() {
    }
}