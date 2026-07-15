package com.infosys.transaction_service.service;

import com.infosys.transaction_service.entity.Account;
import com.infosys.transaction_service.entity.Transaction;
import com.infosys.transaction_service.exception.InsufficientBalanceException;
import com.infosys.transaction_service.feign.AccountFeignClient;
import com.infosys.transaction_service.repository.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.infosys.transaction_service.exception.InsufficientBalanceException;

import java.time.LocalDateTime;

@Service
public class TransactionService {

    @Autowired
    TransactionRepo repo;

    @Autowired
    AccountFeignClient feignClient;

    public Transaction deposit(Integer accId, Double amount) {
        Account account = feignClient.getAccount(accId);
        account.setBalance(account.getBalance() + amount);
        feignClient.updateAccount(account);

        Transaction txn = new Transaction(null, accId, "DEPOSIT", amount, LocalDateTime.now());
        return repo.save(txn);
    }

    public Transaction withdraw(Integer accId, Double amount) {
        Account account = feignClient.getAccount(accId);

        if (account.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance for account " + accId);
        }

        account.setBalance(account.getBalance() - amount);
        feignClient.updateAccount(account);

        Transaction txn = new Transaction(null, accId, "WITHDRAW", amount, LocalDateTime.now());
        return repo.save(txn);
    }
}