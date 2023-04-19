package com.yan.webapp.controller;

import com.timgroup.statsd.StatsDClient;
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

    private final StatsDClient statsDClient;

    public AccountController(AccountService accountService, StatsDClient statsDClient) {
        this.accountService = accountService;
        this.statsDClient = statsDClient;
    }

    Logger logger = LoggerFactory.getLogger(AccountController.class);

    @GetMapping("healthz")
    public ResponseEntity<Void> greet() {
        logger.info("Received request to check application healthz status");

        statsDClient.incrementCounter("endpoint.greet.http.get");

        return ResponseEntity.ok().build();
    }

//    @GetMapping("health")
//    public ResponseEntity<Void> health() {
//        logger.info("Received request to check application health status");
//
//        statsDClient.incrementCounter("endpoint.health.http.get");
//
//        return ResponseEntity.ok().build();
//    }


    @GetMapping("v2/user/{accountId}")
    public AccountDTO getAccount(@PathVariable("accountId") Long id) {
        logger.info("Received request to get account with ID {}", id);
        statsDClient.incrementCounter("endpoint.account.http.get");
        long startTime = System.currentTimeMillis();

        AccountDTO accountDTO = accountService.getAccountById(id);

        long elapsedTime = System.currentTimeMillis() - startTime;
        statsDClient.recordExecutionTime("endpoint.account.http.get.time", elapsedTime);
        logger.info("Returning account: {}", accountDTO);

        return accountDTO;
    }

    @PostMapping("v2/user")
    public ResponseEntity<AccountDTO> createAccount(@Valid @RequestBody Account account){
        logger.info("Received request to create a new account: {}", account);
        statsDClient.incrementCounter("endpoint.account.http.post");
        long startTime = System.currentTimeMillis();

        AccountDTO accountDTO = accountService.createAccount(account);

        long elapsedTime = System.currentTimeMillis() - startTime;
        statsDClient.recordExecutionTime("endpoint.account.http.post.time", elapsedTime);
        logger.info("Created new account: {}", accountDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(accountDTO);
    }

    @PutMapping("v2/user/{accountId}")
    public ResponseEntity<Void>  updateAccount(
            @PathVariable("accountId") Long id,
            @RequestBody AccountUpdateRequest updateRequest
            ) {
        logger.info("Received request to update account with ID {}", id);
        statsDClient.incrementCounter("endpoint.account.http.put");
        long startTime = System.currentTimeMillis();

        accountService.updateAccount(id, updateRequest);

        long elapsedTime = System.currentTimeMillis() - startTime;
        statsDClient.recordExecutionTime("endpoint.account.http.put.time", elapsedTime);
        logger.info("Successfully updated account with ID {}", id);

        return ResponseEntity.noContent().build();
    }
}
