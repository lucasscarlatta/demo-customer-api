package com.assignment.demo.service;

import com.assignment.demo.domain.Account;
import com.assignment.demo.vo.request.AccountRequest;

import java.util.List;
import java.util.UUID;

public interface AccountService {

    void createAccount(AccountRequest accountRequest);

    List<Account> getAllAccounts();

    Account getAccountById(UUID id);

    List<Account> getAllAccountsByCustomerId(UUID customerId);
}
