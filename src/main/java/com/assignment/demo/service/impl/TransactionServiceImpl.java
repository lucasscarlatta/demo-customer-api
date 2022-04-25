package com.assignment.demo.service.impl;

import com.assignment.demo.domain.Account;
import com.assignment.demo.domain.Transaction;
import com.assignment.demo.domain.enums.TransactionType;
import com.assignment.demo.exception.NoContentException;
import com.assignment.demo.repository.TransactionRepository;
import com.assignment.demo.service.AccountService;
import com.assignment.demo.service.TransactionService;
import com.assignment.demo.vo.response.AccountResponse;
import com.assignment.demo.vo.response.CustomerResponse;
import com.assignment.demo.vo.response.TransactionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "Transaction service")
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;
    private final AccountService accountService;
    private final ExecuteTransaction executeTransaction;

    @Override
    public void transaction(UUID accountId, TransactionType transactionType, BigDecimal amount) {
        var account = accountService.getAccountById(accountId);
        switch (transactionType) {
            case DEPOSIT -> executeTransaction.deposit(account, amount);
            case WITHDRAW -> executeTransaction.withdraw(account, amount);
        }
    }

    @Override
    public List<TransactionResponse> getAllTransactions() {
        var transactions = repository.findAll();
        if (transactions.isEmpty()) {
            log.error("Transactions no content");
            throw new NoContentException("Transactions no content");
        }
        return transactions.stream().collect(Collectors.groupingBy(Transaction::getAccount)).entrySet().stream()
                .map(accountListEntry -> createTransaction(accountListEntry.getKey(), accountListEntry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public TransactionResponse getTransactionsByAccountId(UUID accountId) {
        var account = accountService.getAccountById(accountId);
        var transactions = repository.findAllByAccountId(account.getId());

        if (transactions.isEmpty()) {
            var message = String.format("Transactions no content for account id. [AccountId: %s]", accountId);
            log.error(message);
            throw new NoContentException(message);
        }

        return createTransaction(account, transactions);
    }

    private TransactionResponse createTransaction(Account account, List<Transaction> transactions) {
        var customer = account.getCustomer();
        var customerResponse = CustomerResponse.builder().id(customer.getId())
                .name(customer.getName()).surname(customer.getSurname()).build();

        var accountResponse = AccountResponse.builder().id(account.getId())
                .customer(customerResponse).name(account.getName()).balance(account.getBalance()).build();

        return TransactionResponse.builder().account(accountResponse).transactions(transactions).build();
    }

}
