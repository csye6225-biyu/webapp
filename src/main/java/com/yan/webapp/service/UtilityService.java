package com.yan.webapp.service;

import com.yan.webapp.exception.ForbiddenException;
import com.yan.webapp.model.Account;
import com.yan.webapp.repository.AccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UtilityService {
    private final AccountRepository accountRepository;

    public UtilityService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account authenticateUser() {
        //Get owner user from auth
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new ForbiddenException("Authentication failed");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserDetails userDetails)) {
            throw new ForbiddenException("Authentication failed");
        }

        String userName = userDetails.getUsername();

        return accountRepository.findByEmail(userName)
                .orElseThrow(() -> new ForbiddenException("Authentication failed"));
    }
}
