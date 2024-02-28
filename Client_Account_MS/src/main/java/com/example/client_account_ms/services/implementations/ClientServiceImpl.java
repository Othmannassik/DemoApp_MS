package com.example.client_account_ms.services.implementations;

import com.example.client_account_ms.dtos.ClientDTO;
import com.example.client_account_ms.Rtps.RtpRequest;
import com.example.client_account_ms.Rtps.RtpResponse;
import com.example.client_account_ms.entities.Account;
import com.example.client_account_ms.entities.Client;
import com.example.client_account_ms.exceptions.ClientAlreadyExistException;
import com.example.client_account_ms.exceptions.ClientNotFoundException;
import com.example.client_account_ms.exceptions.ClientWithAccountException;
import com.example.client_account_ms.mappers.ClientMapper;
import com.example.client_account_ms.repositories.AccountRepository;
import com.example.client_account_ms.repositories.ClientRepository;
import com.example.client_account_ms.services.interfaces.ClientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
@Transactional
@Slf4j
public class ClientServiceImpl implements ClientService {
    private ClientRepository clientRepository;
    private ClientMapper clientMapper;
    private AccountRepository accountRepository;
    private AmqpAdmin rabbitAdmin;
    private RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper, AccountRepository accountRepository, AmqpAdmin rabbitAdmin, RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.accountRepository = accountRepository;
        this.rabbitAdmin = rabbitAdmin;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Override
    public Client addClient(ClientDTO clientDTO) throws ClientAlreadyExistException {
        Client clientByCni = this.findClientByCni(clientDTO.cni());
        if (clientByCni != null){
            log.warn("Client's cni {} already exist.",clientDTO.cni());
            throw new ClientAlreadyExistException(String.format("Client with cni = %s Already Exist", clientDTO.cni()));
        }
        Client client = clientMapper.toClient(clientDTO);
        log.info("Client {} added.", clientDTO.fullName());

        Queue queue = new Queue(client.getCni(), true, false, false);
        rabbitAdmin.declareQueue(queue);
        log.info("Queue created for Client : {}.", clientDTO.fullName());
        // Bind the queue to the exchange
        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(new DirectExchange(exchangeName)).with(client.getCni()));
        log.info("Binding Exchange with Queue for Client : {}.", clientDTO.fullName());
        return clientRepository.save(client);
    }

    @Override
    public Client updateClient(int clientId, ClientDTO clientDTO) throws ClientNotFoundException {
        this.findClient(clientId);
        Client client = clientMapper.toClient(clientDTO);
        client.setId(clientId);
        log.info("Client {} updated.", clientDTO.fullName());
        return clientRepository.save(client);
    }

    @Override
    public Client findClient(int clientId) throws ClientNotFoundException {
        return clientRepository.findById(clientId)
                .orElseThrow(()-> new ClientNotFoundException(String.format("Client with id = %s Not found", clientId)));
    }

    @Override
    public Client findClientByCni(String cni) {
        return clientRepository.findByCni(cni);
    }

    @Override
    public List<ClientDTO> getClients() {
        return clientRepository.findAll()
                .stream()
                .map(client -> clientMapper.toClientDTO(client))
                .toList();
    }

    @Override
    public void deleteClient(int clientId) throws ClientNotFoundException, ClientWithAccountException {
        Client client = this.findClient(clientId);
        List<Account> accountList = accountRepository.findByClient(client);
        if (accountList.size() > 0){
            log.warn("Client = {} Cannot be deleted. It has at least 1 Account.", clientId);
            throw new ClientWithAccountException(
                    String.format("Client : %s, with Account Cannot be Deleted", clientId));
        }
        log.info("Client = {} deleted.", clientId);
        clientRepository.deleteById(clientId);
    }

    @Override
    public void requestToPay(RtpRequest request) throws ClientNotFoundException, JsonProcessingException {
        Client client = this.findClient(request.clientRequestedId());
        String jsonMessage = objectMapper.writeValueAsString(request);
        log.info("Send RTP to {}.", client.getFullName());
        rabbitTemplate.convertAndSend(exchangeName, client.getCni(), jsonMessage);
    }

    @Override
    public List<RtpResponse> getPayRequests(String queue) throws IOException, ClientNotFoundException {
        List<RtpResponse> rtpResponses = new ArrayList<>();
        while (true) {
            Message message = rabbitTemplate.receive(queue);
            if (message == null) {
                break;
            }
            RtpRequest rtpRequest = objectMapper.readValue(message.getBody(), RtpRequest.class);
            Client client = this.findClient(rtpRequest.clientRequestedId());
            RtpResponse response = new RtpResponse(client.getFullName(), rtpRequest.amount(), rtpRequest.description());
            rtpResponses.add(response);
        }
        log.info("Getting RTP Requests for Client with CNI: {}.", queue);
        return rtpResponses;
    }
}
