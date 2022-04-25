package com.assignment.demo.it.service;

import com.assignment.demo.domain.Account;
import com.assignment.demo.domain.User;
import com.assignment.demo.exception.NoContentException;
import com.assignment.demo.exception.NotFoundException;
import com.assignment.demo.it.AbstractServiceIT;
import com.assignment.demo.repository.AccountRepository;
import com.assignment.demo.repository.UserRepository;
import com.assignment.demo.service.AccountService;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountServiceIT extends AbstractServiceIT {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    @Test
    public void saveAndReadAccount() {
        var name = "Test account";
        var customer = createUser();
        var account = new Account();
        account.setName(name);
        account.setCustomer(customer);

        accountRepository.save(account);

        var accounts = accountService.getAllAccounts();

        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());
        assertAll("Account list", () -> {
            assertEquals(accounts.size(), 1);
            assertAll("First Account", () -> {
                var firstAccount = accounts.get(0);
                assertEquals(name, firstAccount.getName());
                assertEquals(customer, firstAccount.getCustomer());
            });
        });
    }

    @Test
    public void findAccountById() {
        var name = "Test account";
        var customer = createUser();
        var account = new Account();
        account.setName(name);
        account.setCustomer(customer);

        account = accountRepository.save(account);

        var accountResponse = accountService.getAccountById(account.getId());

        assertNotNull(accountResponse);
        assertAll("Account response", () -> {
            assertEquals(name, accountResponse.getName());
            assertEquals(customer, accountResponse.getCustomer());
        });
    }

    @Test
    public void findAccountByIdNotFound() {
        var uuid = UUID.randomUUID();

        var thrown = assertThrows(NotFoundException.class, () -> accountService.getAccountById(uuid));

        assertEquals(String.format("Account not found. [Id: %s]", uuid), thrown.getMessage());
    }

    @Test
    public void findAccountByCustomerId() {
        var name = "Test account";
        var customer = createUser();
        var account = new Account();
        account.setName(name);
        account.setCustomer(customer);

        accountRepository.save(account);

        var accountResponse = accountService.getAllAccountsByCustomerId(customer.getCustomerId());

        assertNotNull(accountResponse);
        assertAll("Account list", () -> {
            assertEquals(accountResponse.size(), 1);
            assertAll("First Account", () -> {
                var firstAccount = accountResponse.get(0);
                assertEquals(name, firstAccount.getName());
                assertEquals(customer, firstAccount.getCustomer());
            });
        });
    }

    @Test
    public void findAccountByCustomerIdNotFound() {
        var uuid = UUID.randomUUID();

        var thrown = assertThrows(NoContentException.class, () -> accountService.getAllAccountsByCustomerId(uuid));

        assertEquals(String.format("Accounts no content for customer id. [CustomerId: %s]", uuid), thrown.getMessage());
    }

    @After
    public void after() {
        accountRepository.deleteAll();
        userRepository.deleteAll();
    }

    private User createUser() {
        var name = "Test name";
        var surname = "Test surname";
        var uuid = UUID.randomUUID();
        var user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setCustomerId(uuid);

        return userRepository.save(user);
    }
}
