package com.example.client_account_ms.services.implementations;

import com.example.client_account_ms.dtos.ClientDTO;
import com.example.client_account_ms.entities.Client;
import com.example.client_account_ms.exceptions.ClientAlreadyExistException;
import com.example.client_account_ms.exceptions.ClientNotFoundException;
import com.example.client_account_ms.mappers.ClientMapper;
import com.example.client_account_ms.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientMapper clientMapper;
    @Mock
    private AmqpAdmin rabbitAdmin;
    @InjectMocks
    private ClientServiceImpl underTest;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres");
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
    @DisplayName("Test For Adding Client")
    void shouldAddClient() throws ClientAlreadyExistException {
        ClientDTO clientDTO= ClientDTO.builder()
                .fullName("Ismail").cni("cni3873").phone("903989834").build();
        Client client= Client.builder()
                .fullName("Ismail").cni("cni3873").phone("903989834").build();
        Client savedClient= Client.builder()
                .fullName("Ismail").cni("cni3873").phone("903989834").build();
        when(clientRepository.findByCni(clientDTO.cni())).thenReturn(null);
        when(clientMapper.toClient(clientDTO)).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(savedClient);
        Client result = underTest.addClient(clientDTO);
        assertThat(result).isNotNull();
        assertThat(savedClient).usingRecursiveComparison()
                .ignoringFields("id","createdAt","updatedAt").isEqualTo(result);
    }
    @Test
    @DisplayName("Test For Not Adding Client Because of duplicate Cni")
    void shouldNotAddClient(){
        ClientDTO clientDTO= ClientDTO.builder()
                .fullName("Ismail").cni("cni3847").phone("903989834").build();
        Client client= Client.builder()
                .fullName("Ismail").cni("cni3847").phone("903989834").build();
        when(clientRepository.findByCni(clientDTO.cni())).thenReturn(client);
        ClientAlreadyExistException exception = assertThrows(ClientAlreadyExistException.class, () -> {
            underTest.addClient(clientDTO);
        });
        String expectedMessage = "Client with cni = cni3847 Already Exist";
        assertEquals(expectedMessage, exception.getMessage());
    }
}