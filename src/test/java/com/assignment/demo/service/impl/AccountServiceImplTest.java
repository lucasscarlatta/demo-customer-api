package com.assignment.demo.service.impl;

import com.assignment.demo.domain.Account;
import com.assignment.demo.domain.User;
import com.assignment.demo.exception.NoContentException;
import com.assignment.demo.exception.NotFoundException;
import com.assignment.demo.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void getAllAccounts() {
        var accounts = Collections.singletonList(new Account());
        when(accountRepository.findAll()).thenReturn(accounts);

        var response = accountService.getAllAccounts();

        assertEquals(response.size(), 1);
        assertEquals(response, accounts);
    }

    @Test
    void getEmptyAccounts() {
        when(accountRepository.findAll()).thenReturn(Collections.emptyList());

        var thrown = assertThrows(NoContentException.class, () -> accountService.getAllAccounts());

        assertEquals("Accounts no content", thrown.getMessage());
    }

    @Test
    void findAccountById() {
        var id = UUID.randomUUID();
        var account = new Account();
        account.setId(id);
        when(accountRepository.findById(id)).thenReturn(Optional.of(account));

        var response = accountService.getAccountById(id);

        assertNotNull(response);
        assertEquals(response.getId(), id);
        assertEquals(response.getBalance(), BigDecimal.ZERO);
    }

    @Test
    void findAccountByIdNotFound() {
        var id = UUID.randomUUID();
        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        var thrown = assertThrows(NotFoundException.class, () -> accountService.getAccountById(id));

        assertEquals(String.format("Account not found. [Id: %s]", id), thrown.getMessage());
    }

    @Test
    void getAllAccountsByCustomerId() {
        var customerId = UUID.randomUUID();
        var user = new User();
        user.setCustomerId(customerId);

        var account = new Account();
        account.setCustomer(user);
        var accounts = Collections.singletonList(account);
        when(accountRepository.findAllByCustomerCustomerId(customerId)).thenReturn(accounts);

        var response = accountService.getAllAccountsByCustomerId(customerId);

        assertEquals(response.size(), 1);
        assertEquals(response, accounts);
    }

    @Test
    void getEmptyAccountsByCustomerId() {
        var customerId = UUID.randomUUID();
        when(accountRepository.findAllByCustomerCustomerId(customerId)).thenReturn(Collections.emptyList());

        var thrown = assertThrows(NoContentException.class, () -> accountService.getAllAccountsByCustomerId(customerId));

        assertEquals(String.format("Accounts no content for customer id. [CustomerId: %s]", customerId), thrown.getMessage());
    }
}
