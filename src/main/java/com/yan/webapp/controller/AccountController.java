package com.yan.webapp.controller;

import com.yan.webapp.dto.AccountDTO;
import com.yan.webapp.dto.AccountUpdateRequest;
import com.yan.webapp.model.Account;
import com.yan.webapp.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("healthz")
    public ResponseEntity<Void> greet() {
        return ResponseEntity.ok().build();
    }


    @GetMapping("v1/user/{accountId}")
    public AccountDTO getAccount(@PathVariable("accountId") Long id) {
        return accountService.getAccountById(id);
    }

    @PostMapping("v1/user")
    public ResponseEntity<AccountDTO> createAccount(@Valid @RequestBody Account account){

        AccountDTO accountDTO = accountService.createAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountDTO);
    }

    @PutMapping("v1/user/{accountId}")
    public ResponseEntity<String>  updateAccount(
            @PathVariable("accountId") Long id,
            @RequestBody AccountUpdateRequest updateRequest
            ) {

        accountService.updateAccount(id, updateRequest);
        return ResponseEntity.noContent().build();
    }
}
