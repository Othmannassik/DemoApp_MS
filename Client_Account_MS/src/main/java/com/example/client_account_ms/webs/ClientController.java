package com.example.client_account_ms.webs;

import com.example.client_account_ms.dtos.ClientDTO;
import com.example.client_account_ms.Rtps.RtpRequest;
import com.example.client_account_ms.Rtps.RtpResponse;
import com.example.client_account_ms.entities.Client;
import com.example.client_account_ms.exceptions.ClientAlreadyExistException;
import com.example.client_account_ms.exceptions.ClientNotFoundException;
import com.example.client_account_ms.exceptions.ClientWithAccountException;
import com.example.client_account_ms.services.interfaces.ClientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/clients")
public class ClientController {
    private ClientService clientService;

    @PostMapping
    public Client addClient(@RequestBody ClientDTO clientDTO) throws ClientAlreadyExistException {
       return clientService.addClient(clientDTO);
    }
    @PutMapping("/{clientId}")
    public Client updateClient(@PathVariable int clientId, @RequestBody ClientDTO clientDTO) throws ClientNotFoundException {
        return clientService.updateClient(clientId, clientDTO);
    }
    @GetMapping
    public List<ClientDTO> clientList(){
        return clientService.getClients();
    }
    @GetMapping("/{clientId}")
    public Client getClient(@PathVariable int clientId) throws ClientNotFoundException {
        return clientService.findClient(clientId);
    }
    @DeleteMapping("/{clientId}")
    public void deleteClient(@PathVariable int clientId) throws ClientNotFoundException, ClientWithAccountException {
        clientService.deleteClient(clientId);
    }
    @PostMapping("/rtp")
    public void requestToPay(@RequestBody RtpRequest request) throws ClientNotFoundException, JsonProcessingException {
        clientService.requestToPay(request);
    }
    @GetMapping("/rtp/{queue}")
    public List<RtpResponse> getPayRequests(@PathVariable String queue) throws IOException, ClientNotFoundException {
        return clientService.getPayRequests(queue);
    }
}
