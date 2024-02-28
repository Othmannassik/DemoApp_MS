package com.example.client_account_ms.services.implementations;

import com.example.client_account_ms.clients.TransactionRestClient;
import com.example.client_account_ms.entities.Account;
import com.example.client_account_ms.entities.Client;
import com.example.client_account_ms.exceptions.AccountNotFoundException;
import com.example.client_account_ms.exceptions.ClientNotFoundException;
import com.example.client_account_ms.exceptions.InsufficientBalanceException;
import com.example.client_account_ms.repositories.AccountRepository;
import com.example.client_account_ms.services.interfaces.AccountService;
import com.example.client_account_ms.services.interfaces.ClientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepository;
    private ClientService clientService;
    private TransactionRestClient transactionRestClient;
    @Override
    public Account addAccount(int clientId) throws ClientNotFoundException {
        Account account = Account.builder()
                .balance(0.0)
                .client(clientService.findClient(clientId))
                .build();
        log.info("Account for client : {} created.", clientId);
        return accountRepository.save(account);
    }

    @Override
    public Account findAccount(int accountId) throws AccountNotFoundException {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(String.format("Account with id = %s Not found", accountId)));
    }

    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public void deleteAccount(int accountId) throws AccountNotFoundException {
        this.findAccount(accountId);
        transactionRestClient.deleteTransactionsByAccount(accountId);
        log.info("Deleting the account = {}.", accountId);
        accountRepository.deleteById(accountId);
    }

    @Override
    public List<Account> accountListByClient(int clientId) throws ClientNotFoundException {
        Client client = clientService.findClient(clientId);
        return accountRepository.findByClient(client);
    }

    @Override
    public void debiter(int accountId, double amount) throws AccountNotFoundException {
        Account account = this.findAccount(accountId);
        account.setBalance(account.getBalance() + amount);
        account.setId(accountId);
        log.info("Debiter the account = {}.", accountId);
        accountRepository.save(account);
    }

    @Override
    public void crediter(int accountId, double amount) throws AccountNotFoundException, InsufficientBalanceException {
        Account account = this.findAccount(accountId);
        if (account.getBalance() < amount){
            log.info("Crediter the account = {} is impossible because of insufficient Balance.", accountId);
            throw new InsufficientBalanceException(String.format("Balance Insufficient For Credit Amount : %s", amount));
        } else {
            account.setBalance(account.getBalance() - amount);
        }
        log.info("Crediter the account = {}.", accountId);
        accountRepository.save(account);
    }
}
