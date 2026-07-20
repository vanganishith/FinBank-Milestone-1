package com.infosys.account_service.controller;

import com.infosys.account_service.entity.Account;
import com.infosys.account_service.entity.AuditLog;
import com.infosys.account_service.repository.AccountRepo;
import com.infosys.account_service.repository.AuditLogRepo;
import com.infosys.account_service.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.infosys.account_service.util.AuthUtil;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountRepo repo;

    @Autowired
    AccountService service;

    @Autowired
    AuditLogRepo auditRepo;

    @Autowired
    AuthUtil authUtil;

    private void log(String action, Integer accId, String details) {
        auditRepo.save(new AuditLog(null, action, accId, details, LocalDateTime.now()));
    }

    @PostMapping("/add")
    public Account addAccount(@RequestBody Account account) {
        account.setStatus("ACTIVE");
        Account saved = repo.save(account);
        log("ACCOUNT_CREATED", saved.getAccId(), "Initial balance: " + saved.getBalance());
        return saved;
    }

    @GetMapping("/all")
    public List<Account> getAllAccounts() {
        return (List<Account>) repo.findAll();
    }

    @GetMapping("/{accId}")
    public Account getAccount(@PathVariable Integer accId) {
        return repo.findById(accId).orElse(null);
    }

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
        log("ACCOUNT_DELETED", accId, "Account removed");
        return "Account deleted: " + accId;
    }

    @PutMapping("/freeze/{accId}")
    public Account freezeAccount(@PathVariable Integer accId, @RequestHeader("Authorization") String authHeader) {
        authUtil.requireRole(authHeader, "MANAGER");
        Account account = repo.findById(accId).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setStatus("FROZEN");
        Account saved = repo.save(account);
        log("ACCOUNT_FROZEN", accId, "Account frozen");
        return saved;
    }

    @PutMapping("/close/{accId}")
    public Account closeAccount(@PathVariable Integer accId, @RequestHeader("Authorization") String authHeader) {
        authUtil.requireRole(authHeader, "MANAGER");
        Account account = repo.findById(accId).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setStatus("CLOSED");
        Account saved = repo.save(account);
        log("ACCOUNT_CLOSED", accId, "Account closed");
        return saved;
    }

    @PutMapping("/reactivate/{accId}")
    public Account reactivateAccount(@PathVariable Integer accId) {
        Account account = repo.findById(accId).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setStatus("ACTIVE");
        Account saved = repo.save(account);
        log("ACCOUNT_REACTIVATED", accId, "Account reactivated");
        return saved;
    }

    @GetMapping("/audit/{accId}")
    public List<AuditLog> getAuditLog(@PathVariable Integer accId) {
        return auditRepo.findByAccId(accId);
    }
}