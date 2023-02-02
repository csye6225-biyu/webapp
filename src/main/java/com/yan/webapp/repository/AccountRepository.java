package com.yan.webapp.repository;

import com.yan.webapp.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository
        extends JpaRepository<Account, Integer> {

    Optional<Account> findByEmail(String email);
    Account findById(Long id);
}
