package com.miguelpazatto.leadsmanager.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.miguelpazatto.leadsmanager.entities.Salesman;
import com.miguelpazatto.leadsmanager.entities.User;
import com.miguelpazatto.leadsmanager.entities.enums.UserRole;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class SalesmanRepositoryTest {

    @Autowired
    private SalesmanRepository salesmanRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Deve retornar Salesman que está a mais tempo sem atender quando existir Salesman no banco de dados")
    void findFirstByOrderByLastLeadDateAsc_WhenSalesmanExist_ReturnSalesman() {
        // given
        User user1 = new User(
                "login",
                "senha",
                UserRole.COLLABORATOR
        );
        userRepository.save(user1);

        Salesman salesman = new Salesman(
                null,
                "Salesman",
                "salesman@email.com",
                "11999999999",
                user1
        );
        salesman.setLastLeadDate(Instant.parse("2026-05-21T08:00:00Z"));
        salesmanRepository.save(salesman);

        User user2 = new User(
                "login2",
                "senha2",
                UserRole.COLLABORATOR
        );
        userRepository.save(user2);

        Salesman oldSalesman = new Salesman(
                null,
                "OldSalesman",
                "oldsalesman@email.com",
                "11888888888",
                user2
        );
        oldSalesman.setLastLeadDate(Instant.parse("2026-05-21T05:00:00Z"));
        salesmanRepository.save(oldSalesman);

        // when
        Optional<Salesman> receivedSalesman = salesmanRepository.findFirstByOrderByLastLeadDateAsc();

        // then
        assertThat(receivedSalesman).isPresent();
        assertThat(receivedSalesman.get().getName()).isEqualTo(oldSalesman.getName());
        assertThat(receivedSalesman.get().getLastLeadDate()).isEqualTo(oldSalesman.getLastLeadDate());
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando não houver nenhum Salesman no banco de dados")
    void findFirstByOrderByLastLeadDateAsc_WhenDatabaseIsEmpty_ReturnsEmptyOptional() {
        // given (sem dados no banco)

        // when
        Optional<Salesman> receivedSalesman = salesmanRepository.findFirstByOrderByLastLeadDateAsc();

        // then
        assertThat(receivedSalesman).isEmpty();
    }

}