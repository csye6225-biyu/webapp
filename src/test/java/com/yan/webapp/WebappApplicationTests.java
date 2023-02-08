package com.yan.webapp;

import com.yan.webapp.model.Account;
import com.yan.webapp.repository.AccountRepository;
import com.yan.webapp.service.AccountService;
import com.yan.webapp.service.UtilityService;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebappApplicationTests {

	@Mock
	private AccountRepository accountRepository;
	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private UtilityService utilityService;

	@Mock
	private Validator validator;
	private AccountService underTest;


	@BeforeEach
	void setUp() {
		underTest = new AccountService(passwordEncoder, accountRepository, utilityService);
	}

	@Test
	void addAccount() {
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

		assertThat(capturedCustomer.getId()).isNull();
		assertThat(capturedCustomer.getEmail()).isEqualTo(account.getEmail());
		assertThat(capturedCustomer.getFirstName()).isEqualTo(account.getFirstName());
		assertThat(capturedCustomer.getLastName()).isEqualTo(account.getLastName());
		assertThat(capturedCustomer.getAccountCreated()).isNull();
		assertThat(capturedCustomer.getAccountUpdated()).isNull();
	}
}
