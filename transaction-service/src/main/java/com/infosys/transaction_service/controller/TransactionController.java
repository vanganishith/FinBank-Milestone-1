package com.infosys.transaction_service.controller;

import com.infosys.transaction_service.entity.Transaction;
import com.infosys.transaction_service.repository.TransactionRepo;
import com.infosys.transaction_service.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    TransactionService service;

    @Autowired
    TransactionRepo repo;

    @PostMapping("/deposit/{accId}/{amount}")
    public Transaction deposit(@PathVariable Integer accId, @PathVariable Double amount) {
        return service.deposit(accId, amount);
    }

    @PostMapping("/withdraw/{accId}/{amount}")
    public Transaction withdraw(@PathVariable Integer accId, @PathVariable Double amount) {
        return service.withdraw(accId, amount);
    }

    @GetMapping("/all")
    public List<Transaction> getAllTransactions() {
        return (List<Transaction>) repo.findAll();
    }

    @GetMapping("/account/{accId}")
    public List<Transaction> getTransactionsByAccount(@PathVariable Integer accId) {
        return repo.findByAccId(accId);
    }
}