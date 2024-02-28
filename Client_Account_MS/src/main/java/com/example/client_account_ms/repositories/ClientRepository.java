package com.example.client_account_ms.repositories;

import com.example.client_account_ms.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    Client findByCni(String cni);
}
