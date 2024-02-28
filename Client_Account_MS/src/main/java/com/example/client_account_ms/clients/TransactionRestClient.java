package com.example.client_account_ms.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "TRANSACTION-SERVICE")
public interface TransactionRestClient {
    @DeleteMapping("/api/transactions/{accountId}")
    void deleteTransactionsByAccount(@PathVariable int accountId);
}
