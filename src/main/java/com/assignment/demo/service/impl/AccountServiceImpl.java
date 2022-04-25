package com.assignment.demo.service.impl;

import com.assignment.demo.domain.Account;
import com.assignment.demo.exception.AlreadyExistsException;
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
        validate(accountRequest.getCustomerId());
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

    private void validate(UUID customerId) {
        if (repository.existsById(customerId)) {
            /*
             * Customer only can have one account due to this constraint
             * Once the endpoint is called, a new account will be opened connected to the user whose ID is customerID.
             */
            throw new AlreadyExistsException(String.format("Account already exist. [AccountId: %s]", customerId));
        }
    }
}
