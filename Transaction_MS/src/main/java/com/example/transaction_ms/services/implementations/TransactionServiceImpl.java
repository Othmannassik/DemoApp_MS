package com.example.transaction_ms.services.implementations;

import com.example.transaction_ms.clients.AccountRestClient;
import com.example.transaction_ms.entities.Transaction;
import com.example.transaction_ms.enums.TransactionType;
import com.example.transaction_ms.excpetions.TypeIsObligatoryException;
import com.example.transaction_ms.models.Account;
import com.example.transaction_ms.repositories.TransactionRepository;
import com.example.transaction_ms.services.interfaces.TransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private TransactionRepository transactionRepository;
    private AccountRestClient accountRestClient;
    @Override
    public Transaction addTransaction(Transaction transaction) throws TypeIsObligatoryException {
        if (transaction.getType() != TransactionType.CREDIT && transaction.getType() != TransactionType.DEBIT) {
            log.info("The Type {} is not Recognized for adding transaction.", transaction.getType().toString());
            throw new TypeIsObligatoryException("Type is Obligatory");
        }
        if (transaction.getType() == TransactionType.CREDIT){
            log.info("Credit From the Account : {}.", transaction.getAccountId());
            accountRestClient.AccountCredit(transaction.getAccountId(), transaction.getAmount());
        } else {
            log.info("Debit To the Account : {}.", transaction.getAccountId());
            accountRestClient.AccountDebit(transaction.getAccountId(), transaction.getAmount());
        }
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> transactionList() {
        List<Transaction> transactionList = transactionRepository.findAll();
        transactionList.stream()
                .forEach(transaction -> {
                    Account account = accountRestClient.getAccount(transaction.getAccountId());
                    transaction.setAccount(account);
                });
        return transactionList;
    }

    @Override
    public List<Transaction> transactionListByAccount(int accountId) {
        Account account = accountRestClient.getAccount(accountId);
        List<Transaction> transactionList = transactionRepository.findByAccountId(accountId);
        transactionList.stream()
                .forEach(transaction -> transaction.setAccount(account));
        return transactionList;
    }

    @Override
    public void deleteTransactionListByAccount(int accountId) {
        List<Transaction> transactionList = this.transactionListByAccount(accountId);
        log.info("Deleting the Transactions for the Account : {}.", accountId);
        transactionList.stream()
                .forEach(transaction -> transactionRepository.deleteById(transaction.getId()));
    }
}
