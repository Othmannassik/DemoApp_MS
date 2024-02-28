package com.example.client_account_ms.repositories;

import com.example.client_account_ms.entities.Account;
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

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountRepositoryTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres");
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Test
    @DisplayName("Test Connection For AccountRepositoryTest")
    public void connectionEstablishedTest(){
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }
    @BeforeEach
    void setUp() {
        Client client = clientRepository.save(Client.builder()
                .fullName("Othman").cni("cni3847").phone("067383837784").build());
        accountRepository.save(Account.builder().balance(0.0).client(client).build());
    }

    @Test
    @DisplayName("Test For find Account by Client")
    void shouldFindAccountByClient() {
        Client client = clientRepository.findByCni("cni3847");
        List<Account> result = accountRepository.findByClient(client);
        assertThat(result).isNotNull();
        assertEquals(result.size(),1);
    }

    @Test
    @DisplayName("Test For not finding Account by Client")
    void shouldNotFindAccountByClient() {
        Client client = clientRepository.findByCni("cni3879");
        List<Account> result = accountRepository.findByClient(client);
        assert(result).isEmpty();
        assertEquals(result.size(),0);
    }
}