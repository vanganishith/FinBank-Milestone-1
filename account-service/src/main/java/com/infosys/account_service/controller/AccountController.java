package com.infosys.account_service.controller;

import com.infosys.account_service.entity.Account;
import com.infosys.account_service.repository.AccountRepo;
import com.infosys.account_service.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountRepo repo;

    @Autowired
    AccountService service;

    @PostMapping("/add")
    public Account addAccount(@RequestBody Account account) {
        return repo.save(account);
    }

    @GetMapping("/all")
    public List<Account> getAllAccounts() {
        return (List<Account>) repo.findAll();
    }

    @GetMapping("/{accId}")
    public Account getAccount(@PathVariable Integer accId) {
        return repo.findById(accId).orElse(null);
    }

    // this one calls Customer Service via Feign
    @GetMapping("/withCustomer/{accId}")
    public Account getAccountWithCustomerDetails(@PathVariable Integer accId) {
        return service.getAccountWithCustomer(accId);
    }

    @PutMapping("/update")
    public Account updateAccount(@RequestBody Account account) {
        return repo.save(account);
    }

    @DeleteMapping("/delete/{accId}")
    public String deleteAccount(@PathVariable Integer accId) {
        repo.deleteById(accId);
        return "Account deleted: " + accId;
    }
}