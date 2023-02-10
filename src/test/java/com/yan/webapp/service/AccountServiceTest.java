package com.yan.webapp.service;

import com.yan.webapp.dto.AccountDTO;
import com.yan.webapp.model.Account;
import com.yan.webapp.repository.AccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UtilityService utilityService;

    private AccountService underTest;

    ModelMapper modelMapper = new ModelMapper();

    @BeforeEach
    void setUp() {
        underTest = new AccountService(passwordEncoder, accountRepository, utilityService);
    }

    @Test
    void canGetAccountById() {
        // Given
        Long id = 10L;
        Account account = new Account(
                "alex@gmail.com", "password",
                "Alex", "Strong");
        account.setId(10L);
        when(utilityService.authenticateUser()).thenReturn(account);

        when(accountRepository.findById(id)).thenReturn(account);

        AccountDTO expected = modelMapper.map(account, AccountDTO.class);

        // When
        AccountDTO actual = underTest.getAccountById(id);

        // Then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void canAddAccount() {
        // Given
        String email = "alex@gmail.com";

        Account account = new Account(
                email, "password", "Alex", "Xu"
        );

        // When
        underTest.createAccount(account);

        // Then
        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(
                Account.class
        );

        verify(accountRepository).save(accountArgumentCaptor.capture());

        Account capturedCustomer = accountArgumentCaptor.getValue();


        Assertions.assertThat(capturedCustomer)
                .usingRecursiveComparison()
                .isEqualTo(account);

    }

    @Test
    void updateAccount() {
    }
}