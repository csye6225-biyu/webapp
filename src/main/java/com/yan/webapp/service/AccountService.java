package com.yan.webapp.service;

import com.yan.webapp.exception.BadRequestException;
import com.yan.webapp.exception.ForbiddenException;
import com.yan.webapp.exception.ResourceNotFoundException;
import com.yan.webapp.exception.UnauthorizedException;
import com.yan.webapp.model.Account;
import com.yan.webapp.repository.AccountRepository;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@EnableJpaAuditing
public class AccountService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    public AccountService(PasswordEncoder passwordEncoder, AccountRepository accountRepository) {
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
    }


    /* Get all accounts. */
    public List<Account> getAllAccounts () {
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
//            account.setPassword("");
        }
        return accountRepository.findAll();
    }

    /* Get an account by id. */
    public Account getAccountById(Long id) {
        authenticateUser(id);

        Account account = accountRepository.findById(id);
        if (account == null) {
            throw new ResourceNotFoundException(
                    "account with id [%s] not found".formatted(id));
        }
        account.setPassword("");
        return account;
    }

    /* Create an account. */
    public boolean createAccount(Account account) {

        // Encode password
        account.setPassword(passwordEncoder.encode(account.getPassword()));

        // Save account
        accountRepository.save(account);
        return true;
    }

    public record AccountUpdateRequest(
            String firstName,
            String lastName,
            String password
    ) {}


    /* Update account information. */
    public boolean updateAccount(Long id,
                                 AccountUpdateRequest updateRequest) {

        authenticateUser(id);

        Account account = accountRepository.findById(id);
//        if (account == null) {
//            throw new ResourceNotFoundException(
//                    "account with id [%s] not found".formatted(id));
//        }



        if (updateRequest.firstName() != null && !updateRequest.firstName().equals(account.getFirstName())){
            account.setFirstName(updateRequest.firstName());
        }

        if (updateRequest.lastName() != null && !updateRequest.lastName().equals(account.getLastName())){
            account.setLastName(updateRequest.lastName());
        }

        if (updateRequest.password() != null && !updateRequest.password().equals(account.getPassword())){
            account.setPassword(passwordEncoder.encode(updateRequest.password()));
        }


        accountRepository.save(account);
        return true;
    }

    public void authenticateUser(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                // access user details, such as username, authorities, etc.
                System.out.println("UserDetails = " + userDetails.getUsername());
                String userName = userDetails.getUsername();
                Account accountExists = accountRepository.findByEmail(userName)
                        .orElseThrow(() -> new UnauthorizedException("unauthorized"));
                if (accountExists.getId() != id) {
                    throw new ForbiddenException("forbidden");
                }
            }
        }

    }

}
