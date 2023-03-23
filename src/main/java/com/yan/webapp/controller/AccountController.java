package com.yan.webapp.controller;

import com.yan.webapp.dto.AccountDTO;
import com.yan.webapp.dto.AccountUpdateRequest;
import com.yan.webapp.model.Account;
import com.yan.webapp.service.AccountService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@Validated
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        logger.info("Received request to check application health status");
        this.accountService = accountService;
    }

    Logger logger = LoggerFactory.getLogger(AccountController.class);

    @GetMapping("healthz")
    public ResponseEntity<Void> greet() {
        return ResponseEntity.ok().build();
    }


    @GetMapping("v1/user/{accountId}")
    public AccountDTO getAccount(@PathVariable("accountId") Long id) {
        logger.info("Received request to get account with ID {}", id);
        AccountDTO accountDTO = accountService.getAccountById(id);
        logger.info("Returning account: {}", accountDTO);
        return accountDTO;
    }

    @PostMapping("v1/user")
    public ResponseEntity<AccountDTO> createAccount(@Valid @RequestBody Account account){
        logger.info("Received request to create a new account: {}", account);
        AccountDTO accountDTO = accountService.createAccount(account);
        logger.info("Created new account: {}", accountDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountDTO);
    }

    @PutMapping("v1/user/{accountId}")
    public ResponseEntity<Void>  updateAccount(
            @PathVariable("accountId") Long id,
            @RequestBody AccountUpdateRequest updateRequest
            ) {
        logger.info("Received request to update account with ID {}", id);
        accountService.updateAccount(id, updateRequest);
        logger.info("Successfully updated account with ID {}", id);
        return ResponseEntity.noContent().build();
    }
}
