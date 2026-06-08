package com.miguelpazatto.leadsmanager.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miguelpazatto.leadsmanager.dto.SalesmanUpdateDTO;
import com.miguelpazatto.leadsmanager.entities.Salesman;
import com.miguelpazatto.leadsmanager.entities.User;
import com.miguelpazatto.leadsmanager.entities.enums.UserRole;
import com.miguelpazatto.leadsmanager.repositories.SalesmanRepository;
import com.miguelpazatto.leadsmanager.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class SalesmanResourceIT {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private SalesmanRepository salesmanRepository;

    @Autowired
    private UserRepository userRepository;

    private Salesman salesman;

    @BeforeEach
    void setUp() {
        User user = new User(
                "login",
                "senha",
                UserRole.COLLABORATOR
        );
        userRepository.save(user);

        Salesman salesman = new Salesman(
                null,
                "Salesman",
                "salesman@email.com",
                "11999999999",
                user
        );
        this.salesman = salesmanRepository.save(salesman);
    }

    @Test
    @DisplayName("Deve retornar Status 200 (OK) quando a lista de salesmen não for vazia")
    void findAll_WhenListIsNotEmpty_ReturnOk() throws Exception {

        mockMvc.perform(get("/salesman")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("Deve retornar Status 200 (OK) quando houver ID correspondente ao salesman")
    void findById_WhenIdExist_ReturnOk() throws Exception {
        Long id = salesman.getId();

        mockMvc.perform(get("/salesman/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    @DisplayName("Deve retornar Status 200 (No Content) quando deletar um salesman")
    void deleteSalesman_WhenIdExist_ReturnNoContent() throws Exception {
        Long id = salesman.getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/salesman/{id}", id))
                .andExpect(status().isNoContent());

        boolean stillExists = salesmanRepository.existsById(id);
        assertFalse(stillExists, "Salesman não deltado do banco de dados");
    }

    @Test
    void update() throws Exception {
        SalesmanUpdateDTO salesmanUpdateDTO = new SalesmanUpdateDTO(
                "Updated Salesman",
                "updatedsalesman@email.com",
                "11888888888"
        );

        mockMvc.perform(put("/salesman/{id}", salesman.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(salesmanUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(salesmanUpdateDTO.name()))
                .andExpect(jsonPath("$.email").value(salesmanUpdateDTO.email()))
                .andExpect(jsonPath("$.phone").value(salesmanUpdateDTO.phone()))
                .andDo(print());

        Salesman savedSalesman = salesmanRepository.findById(salesman.getId()).get();
        assertEquals(savedSalesman.getName(), salesmanUpdateDTO.name());
    }
}