package com.assignment.demo.service.impl;

import com.assignment.demo.domain.Account;
import com.assignment.demo.exception.NoContentException;
import com.assignment.demo.exception.NotFoundException;
import com.assignment.demo.repository.AccountRepository;
import com.assignment.demo.service.AccountService;
import com.assignment.demo.service.UserService;
import com.assignment.demo.vo.request.AccountRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j(topic = "Account service")
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;
    private final UserService userService;

    @Override
    public void createAccount(AccountRequest accountRequest) {
        var account = new Account();
        var customer = userService.getUserByCustomerId(accountRequest.getCustomerId());
        account.setCustomer(customer);
        account.setName(accountRequest.getName());

        // TODO add initial initialCredit

        repository.save(account);
    }

    @Override
    public List<Account> getAllAccounts() {
        var accounts = repository.findAll();
        if (accounts.isEmpty()) {
            log.error("Accounts no content");
            throw new NoContentException("Accounts no content");
        }
        return accounts;
    }

    @Override
    public Account getAccountById(UUID id) {
        return repository.findById(id).orElseThrow(() -> {
            var message = String.format("Account not found. [Id: %s]", id);
            log.error(message);
            throw new NotFoundException(message);
        });
    }

    @Override
    public List<Account> getAllAccountsByCustomerId(UUID customerId) {
        var accounts = repository.findAllByCustomerCustomerId(customerId);
        if (accounts.isEmpty()) {
            var message = String.format("Accounts no content for customer id. [CustomerId: %s]", customerId);
            log.error(message);
            throw new NoContentException(message);
        }
        return accounts;
    }
}
