package com.example.client_account_ms.mappers;

import com.example.client_account_ms.dtos.ClientDTO;
import com.example.client_account_ms.entities.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ClientMapper {
    public Client toClient(ClientDTO clientDTO){
        Client client = new Client();
        BeanUtils.copyProperties(clientDTO, client);
        log.info("Client "+ clientDTO.fullName()+" mapped From DTO.");
        return client;
    }
    public ClientDTO toClientDTO(Client client){
        ClientDTO clientDTO =
                new ClientDTO(client.getId(), client.getFullName(), client.getCni(), client.getPhone());
        return clientDTO;
    }
}
