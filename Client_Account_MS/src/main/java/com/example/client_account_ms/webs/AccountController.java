package com.example.client_account_ms.webs;

import com.example.client_account_ms.entities.Account;
import com.example.client_account_ms.exceptions.AccountNotFoundException;
import com.example.client_account_ms.exceptions.ClientNotFoundException;
import com.example.client_account_ms.exceptions.InsufficientBalanceException;
import com.example.client_account_ms.services.interfaces.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {
    private AccountService accountService;
    @PostMapping("/{clientId}")
    public Account addAccount(@PathVariable int clientId) throws ClientNotFoundException {
        return accountService.addAccount(clientId);
    }
    @GetMapping
    public List<Account> accountList(){
        return accountService.getAccounts();
    }
    @GetMapping("/{accountId}")
    public Account getAccount(@PathVariable int accountId) throws AccountNotFoundException {
        return accountService.findAccount(accountId);
    }
    @GetMapping("/client/{clientId}")
    public List<Account> accountListByClient(@PathVariable int clientId) throws ClientNotFoundException {
        return accountService.accountListByClient(clientId);
    }
    @DeleteMapping("/{accountId}")
    public void deleteAccount(@PathVariable int accountId) throws AccountNotFoundException {
        accountService.deleteAccount(accountId);
    }

    @PostMapping("/debit/{accountId}")
    public void debiter(@PathVariable int accountId, @RequestBody double amount) throws AccountNotFoundException {
        accountService.debiter(accountId, amount);
    }

    @PostMapping("/credit/{accountId}")
    public void crediter(@PathVariable int accountId, @RequestBody double amount) throws AccountNotFoundException, InsufficientBalanceException {
        accountService.crediter(accountId, amount);
    }
}
