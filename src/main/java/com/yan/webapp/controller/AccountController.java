package com.yan.webapp.controller;

import com.yan.webapp.model.Account;
import com.yan.webapp.repository.AccountRepository;
import com.yan.webapp.service.AccountDTO;
import com.yan.webapp.service.AccountService;
import com.yan.webapp.service.AccountUpdateRequest;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@Validated
public class AccountController {

    private final AccountService accountService;
    private final AccountRepository accountRepository;


    public AccountController(AccountService accountService, AccountRepository accountRepository) {
        this.accountService = accountService;
        this.accountRepository = accountRepository;
    }

    public static boolean patternMatches(String emailAddress) {
        String regexPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
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
        System.out.println(patternMatches(account.getEmail()));
        if (!patternMatches(account.getEmail())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Account> accountExists = accountRepository.findByEmail(account.getEmail());
        if (accountExists.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        AccountDTO accountDTO = accountService.createAccount(account);

        return ResponseEntity.status(HttpStatus.CREATED).body(accountDTO);
    }

    @PutMapping("v1/user/{accountId}")
    public ResponseEntity<String>  updateAccount(
            @PathVariable("accountId") Long id,
            @RequestBody AccountUpdateRequest updateRequest
            ) {
        System.out.println(updateRequest);

        if (updateRequest.password() == null
                && updateRequest.firstName() == null
                && updateRequest.lastName() == null ){
            return ResponseEntity.badRequest().body("");
        }

        accountService.updateAccount(id, updateRequest);
        return ResponseEntity.noContent().build();
    }
}
