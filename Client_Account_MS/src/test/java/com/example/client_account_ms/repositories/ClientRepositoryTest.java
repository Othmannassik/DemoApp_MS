package com.example.client_account_ms.repositories;

import com.example.client_account_ms.entities.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.*;
@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClientRepositoryTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres");
    @Autowired
    private ClientRepository clientRepository;

    @Test
    @DisplayName("Test Connection For ClientRepositoryTest")
    public void connectionEstablishedTest(){
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }
    @BeforeEach
    void setUp() {
        clientRepository.save(Client.builder()
                .fullName("Othman").cni("cni3847").phone("067383837784").build());
    }

    @Test
    @DisplayName("Test For find client by cni")
    void shouldFindClientByCni() {
        String givenCni="cni3847";
        Client expected=Client.builder().fullName("Othman").cni("cni3847").phone("067383837784").build();
        Client result = clientRepository.findByCni(givenCni);
        assertThat(result).isNotNull();
        assertThat(expected).usingRecursiveComparison()
                .ignoringFields("id","createdAt","updatedAt").isEqualTo(result);
    }

    @Test
    @DisplayName("Test for not finding client by cni")
    void shouldNotFindClientByCni() {
        String givenCni="cni49840";
        Client result = clientRepository.findByCni(givenCni);
        assertThat(result).isNull();
    }
}