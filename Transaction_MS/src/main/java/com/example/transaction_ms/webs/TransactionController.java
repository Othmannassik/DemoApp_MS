package com.example.transaction_ms.webs;

import com.example.transaction_ms.entities.Transaction;
import com.example.transaction_ms.excpetions.TypeIsObligatoryException;
import com.example.transaction_ms.services.interfaces.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/transactions")
public class TransactionController {
    private TransactionService transactionService;
    @PostMapping
    public Transaction addTransaction(@RequestBody Transaction transaction) throws TypeIsObligatoryException {
        return transactionService.addTransaction(transaction);
    }
    @GetMapping
    public List<Transaction> transactionList(){
        return transactionService.transactionList();
    }
    @GetMapping("/{accountId}")
    public List<Transaction> transactionListByAccount(@PathVariable int accountId){
        return transactionService.transactionListByAccount(accountId);
    }
    @DeleteMapping("/{accountId}")
    public void deleteTransactionsByAccount(@PathVariable int accountId){
        transactionService.deleteTransactionListByAccount(accountId);
    }
}
