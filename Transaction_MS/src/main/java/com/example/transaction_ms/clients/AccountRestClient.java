package com.example.transaction_ms.clients;

import com.example.transaction_ms.models.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ACCOUNT-SERVICE")
public interface AccountRestClient {
    @GetMapping("/api/accounts/{accountId}")
    Account getAccount(@PathVariable int accountId);
    @PostMapping("/api/accounts/credit/{accountId}")
    void AccountCredit(@PathVariable int accountId, @RequestBody double amount);
    @PostMapping("/api/accounts/debit/{accountId}")
    void AccountDebit(@PathVariable int accountId, @RequestBody double amount);
}
