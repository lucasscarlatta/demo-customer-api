package com.assignment.demo.controller;

import com.assignment.demo.domain.Account;
import com.assignment.demo.service.AccountService;
import com.assignment.demo.vo.request.AccountRequest;
import lombok.RequiredArgsConstructor;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1/accounts")
@Validated
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    Account createAccount(@RequestBody @Valid @NotNull AccountRequest accountRequest) {
        var stopWatch = new Slf4JStopWatch(String.format("Create account for [customerId: %s]", accountRequest.getCustomerId()));
        try {
            return accountService.createAccount(accountRequest);
        } finally {
            stopWatch.stop();
        }
    }

    @GetMapping
    List<Account> getAllAccounts() {
        var stopWatch = new Slf4JStopWatch("Get all accounts");
        try {
            return accountService.getAllAccounts();
        } finally {
            stopWatch.stop();
        }
    }

    @GetMapping("{id}")
    Account getAccountById(@PathVariable UUID id) {
        var stopWatch = new Slf4JStopWatch(String.format("Get account by [Id: %s]", id));
        try {
            return accountService.getAccountById(id);
        } finally {
            stopWatch.stop();
        }
    }

    @GetMapping("customers/{customerId}")
    List<Account> getAccountsByCustomerId(@PathVariable UUID customerId) {
        var stopWatch = new Slf4JStopWatch(String.format("Get accounts by customerId [CustomerId: %s]", customerId));
        try {
            return accountService.getAllAccountsByCustomerId(customerId);
        } finally {
            stopWatch.stop();
        }
    }
}



