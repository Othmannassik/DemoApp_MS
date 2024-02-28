package com.example.transaction_ms.services.interfaces;

import com.example.transaction_ms.entities.Transaction;
import com.example.transaction_ms.excpetions.TypeIsObligatoryException;

import java.util.List;

public interface TransactionService {
    Transaction addTransaction(Transaction transaction) throws TypeIsObligatoryException;
    List<Transaction> transactionList();
    List<Transaction> transactionListByAccount(int accountId);
    void deleteTransactionListByAccount(int accountId);
}
