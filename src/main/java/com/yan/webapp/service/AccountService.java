package com.yan.webapp.service;

import com.yan.webapp.dto.AccountDTO;
import com.yan.webapp.dto.AccountUpdateRequest;
import com.yan.webapp.exception.BadRequestException;
import com.yan.webapp.exception.ForbiddenException;
import com.yan.webapp.exception.ResourceNotFoundException;
import com.yan.webapp.model.Account;
import com.yan.webapp.repository.AccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@EnableJpaAuditing
public class AccountService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    public AccountService(PasswordEncoder passwordEncoder, AccountRepository accountRepository) {
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
    }

    ModelMapper modelMapper = new ModelMapper();

    /** Get an account by id. */
    public AccountDTO getAccountById(Long id) {

        authenticateUser(id);

        Account account = accountRepository.findById(id);
        if (account == null) {
            throw new ResourceNotFoundException(
                    "account with id [%s] not found".formatted(id));
        }
        account.setPassword("");
        return modelMapper.map(account, AccountDTO.class);
    }

    /** Create an account. */
    public AccountDTO createAccount(Account account) {
        //Check if email already exist
        Optional<Account> accountExists = accountRepository.findByEmail(account.getEmail());
        if (accountExists.isPresent()) {
            throw new BadRequestException("Email already registered");
        }
        // Encode password
        account.setPassword(passwordEncoder.encode(account.getPassword()));

        // Save account
        accountRepository.save(account);
        return modelMapper.map(account, AccountDTO.class);
    }



    /** Update account information. */
    public boolean updateAccount(Long id,
                                 AccountUpdateRequest updateRequest) {

        authenticateUser(id);

        Account account = accountRepository.findById(id);

        boolean changes = false;

        if (updateRequest.firstName() != null && updateRequest.firstName().length() != 0 && !updateRequest.firstName().equals(account.getFirstName())){
            account.setFirstName(updateRequest.firstName());
            changes = true;
        }

        if (updateRequest.lastName() != null && updateRequest.lastName().length() != 0 && !updateRequest.lastName().equals(account.getLastName())){
            account.setLastName(updateRequest.lastName());
            changes = true;
        }

        if (updateRequest.password() != null && updateRequest.password().length() != 0 && !updateRequest.password().equals(account.getPassword())){
            account.setPassword(passwordEncoder.encode(updateRequest.password()));
            changes = true;
        }

        if (!changes) {
            throw new BadRequestException("no data changes found");
        }

        accountRepository.save(account);
        return true;
    }

    /**
     * Authenticate user: get token and get username from the token,
     * compare with request id to prevent forbidden request*/
    public void authenticateUser(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                // access user details, such as username, authorities, etc.
                String userName = userDetails.getUsername();
                Account accountExists = accountRepository.findByEmail(userName)
                        .orElse(null);
                if (accountExists.getId() != id) {
                    throw new ForbiddenException("You are not allowed to access other people's data");
                }
            }
        }

    }

}
