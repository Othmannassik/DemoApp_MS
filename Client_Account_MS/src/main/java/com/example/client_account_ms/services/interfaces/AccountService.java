package com.example.client_account_ms.services.interfaces;

import com.example.client_account_ms.entities.Account;
import com.example.client_account_ms.exceptions.AccountNotFoundException;
import com.example.client_account_ms.exceptions.ClientNotFoundException;
import com.example.client_account_ms.exceptions.InsufficientBalanceException;

import java.util.List;

public interface AccountService {
    Account addAccount(int clientId) throws ClientNotFoundException;
    Account findAccount(int accountId) throws AccountNotFoundException;
    List<Account> getAccounts();
    void deleteAccount(int accountId) throws AccountNotFoundException;
    List<Account> accountListByClient(int clientId) throws ClientNotFoundException;
    void debiter(int accountId, double amount) throws AccountNotFoundException;
    void crediter(int accountId, double amount) throws AccountNotFoundException, InsufficientBalanceException;
}
