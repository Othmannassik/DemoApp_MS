package com.example.client_account_ms.repositories;

import com.example.client_account_ms.entities.Account;
import com.example.client_account_ms.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<Account> findByClient(Client client);
}
