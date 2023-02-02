package com.yan.webapp.controller;

import com.yan.webapp.exception.ResourceNotFoundException;
import com.yan.webapp.exception.UnauthorizedException;
import com.yan.webapp.model.Account;
import com.yan.webapp.repository.AccountRepository;
import com.yan.webapp.service.AccountService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
public class AccountController {

    private final AccountService accountService;
    private final AccountRepository accountRepository;

    public AccountController(AccountService accountService, AccountRepository accountRepository) {
        this.accountService = accountService;
        this.accountRepository = accountRepository;
    }

    @GetMapping("healthz")
    public ResponseEntity<Void> greet() {
        return ResponseEntity.ok().build();
    }

//    @GetMapping("v1/user")
//    public List<Account> getAccounts() {
//        return accountService.getAllAccounts();
//    }

    @GetMapping("v1/user/{accountId}")
    public Account getAccount(@PathVariable("accountId") Long id) {
        return accountService.getAccountById(id);
    }

    @PostMapping("v1/user")
    public ResponseEntity<String> createAccount(@RequestBody Account account){
        Optional<Account> accountExists = accountRepository.findByEmail(account.getEmail());
        if (accountExists.isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        accountService.createAccount(account);

        return ResponseEntity.status(HttpStatus.CREATED).body("Account created");
    }

    @PutMapping("v1/user/{accountId}")
    public ResponseEntity<String>  updateAccount(
            @PathVariable("accountId") Long id,
            @RequestBody AccountService.AccountUpdateRequest updateRequest
            ) {
        System.out.println(updateRequest);

        if (updateRequest.password() == null
                && updateRequest.firstName() == null
                && updateRequest.lastName() == null ){
            return ResponseEntity.badRequest().body("nothing changed");
        }

        accountService.updateAccount(id, updateRequest);
        return ResponseEntity.noContent().build();
    }
}
