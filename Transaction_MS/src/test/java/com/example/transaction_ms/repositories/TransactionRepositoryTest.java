package com.example.transaction_ms.repositories;

import com.example.transaction_ms.entities.Transaction;
import com.example.transaction_ms.enums.TransactionType;
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
class TransactionRepositoryTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres");
    @Autowired
    private TransactionRepository transactionRepository;
    @Test
    @DisplayName("Test Connection For TransactionRepositoryTest")
    public void connectionEstablishedTest(){
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }
    @BeforeEach
    void setUp() {
        transactionRepository.save(Transaction.builder().accountId(1).amount(1000.0).build());
    }

    @Test
    @DisplayName("Test For find transactions by accountId")
    void shouldFindTransactionsByAccountId() {
        int givenAccountId=1;
        List<Transaction> result = transactionRepository.findByAccountId(givenAccountId);
        assertThat(result).isNotNull();
        assertEquals(result.size(), 1);
    }

    @Test
    @DisplayName("Test For not finding transactions by accountId")
    void shouldNotFindTransactionsByAccountId() {
        int givenAccountId=3;
        List<Transaction> result = transactionRepository.findByAccountId(givenAccountId);
        assert(result).isEmpty();
        assertEquals(result.size(), 0);
    }
}