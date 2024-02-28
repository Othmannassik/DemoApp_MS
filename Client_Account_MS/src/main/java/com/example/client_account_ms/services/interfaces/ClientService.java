package com.example.client_account_ms.services.interfaces;

import com.example.client_account_ms.dtos.ClientDTO;
import com.example.client_account_ms.Rtps.RtpRequest;
import com.example.client_account_ms.Rtps.RtpResponse;
import com.example.client_account_ms.entities.Client;
import com.example.client_account_ms.exceptions.ClientAlreadyExistException;
import com.example.client_account_ms.exceptions.ClientNotFoundException;
import com.example.client_account_ms.exceptions.ClientWithAccountException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;

public interface ClientService {
    Client addClient(ClientDTO clientDTO) throws ClientAlreadyExistException;
    Client updateClient(int clientId, ClientDTO clientDTO) throws ClientNotFoundException;
    Client findClient(int clientId) throws ClientNotFoundException;
    Client findClientByCni(String cni);
    List<ClientDTO> getClients();
    void deleteClient(int clientId) throws ClientNotFoundException, ClientWithAccountException;
    void requestToPay(RtpRequest request) throws ClientNotFoundException, JsonProcessingException;
    List<RtpResponse> getPayRequests(String queue) throws IOException, ClientNotFoundException;
}
