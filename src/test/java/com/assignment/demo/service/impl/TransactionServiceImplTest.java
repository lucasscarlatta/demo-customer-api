package com.assignment.demo.service.impl;

import com.assignment.demo.domain.Account;
import com.assignment.demo.domain.Transaction;
import com.assignment.demo.domain.User;
import com.assignment.demo.domain.enums.TransactionType;
import com.assignment.demo.exception.NoContentException;
import com.assignment.demo.repository.TransactionRepository;
import com.assignment.demo.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private ExecuteTransaction executeTransaction;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void getAllTransactions() {
        var customer = new User();
        customer.setName("Customer");
        var account = new Account();
        account.setBalance(BigDecimal.TEN);
        account.setCustomer(customer);

        var transactions = new ArrayList<Transaction>();
        var transaction = new Transaction();
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setTime(LocalDateTime.now());
        transaction.setAmount(BigDecimal.TEN);
        transaction.setAccount(account);
        transactions.add(transaction);
        transaction = new Transaction();
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setTime(LocalDateTime.now());
        transaction.setAmount(BigDecimal.TEN);
        transaction.setAccount(account);
        transactions.add(transaction);

        when(transactionRepository.findAll()).thenReturn(transactions);

        var response = transactionService.getAllTransactions();

        assertEquals(response.size(), 1);
    }

    @Test
    void getEmptyTransactions() {
        when(transactionRepository.findAll()).thenReturn(Collections.emptyList());

        var thrown = assertThrows(NoContentException.class, () -> transactionService.getAllTransactions());

        assertEquals("Transactions no content", thrown.getMessage());
    }

    @Test
    void getAllTransactionsByCustomerId() {
        var customer = new User();
        var accountId = UUID.randomUUID();
        var account = new Account();
        account.setId(accountId);
        account.setCustomer(customer);

        var transaction = new Transaction();
        transaction.setAccount(account);
        var transactions = Collections.singletonList(transaction);

        when(accountService.getAccountById(accountId)).thenReturn(account);
        when(transactionRepository.findAllByAccountId(accountId)).thenReturn(transactions);

        var response = transactionService.getTransactionsByAccountId(accountId);

        assertEquals(response.getTransactions().size(), 1);
        assertEquals(response.getAccount().getId(), accountId);
    }

    @Test
    void getEmptyTransactionsByCustomerId() {
        var accountId = UUID.randomUUID();
        var account = new Account();
        account.setId(accountId);

        when(accountService.getAccountById(accountId)).thenReturn(account);
        when(transactionRepository.findAllByAccountId(accountId)).thenReturn(Collections.emptyList());

        var thrown = assertThrows(NoContentException.class, () -> transactionService.getTransactionsByAccountId(accountId));

        assertEquals(String.format("Transactions no content for account id. [AccountId: %s]", accountId), thrown.getMessage());
    }

    @Test
    void executionDepositTransaction() {
        var accountId = UUID.randomUUID();
        var account = new Account();
        account.setId(accountId);

        when(accountService.getAccountById(accountId)).thenReturn(account);
        doNothing().when(executeTransaction).deposit(account, BigDecimal.TEN);

        transactionService.transaction(accountId, TransactionType.DEPOSIT, BigDecimal.TEN);

        verify(executeTransaction, times(1)).deposit(account, BigDecimal.TEN);
        verify(executeTransaction, never()).withdraw(account, BigDecimal.TEN);
    }

    @Test
    void executionWithdrawTransaction() {
        var accountId = UUID.randomUUID();
        var account = new Account();
        account.setId(accountId);

        when(accountService.getAccountById(accountId)).thenReturn(account);
        doNothing().when(executeTransaction).withdraw(account, BigDecimal.TEN);

        transactionService.transaction(accountId, TransactionType.WITHDRAW, BigDecimal.TEN);

        verify(executeTransaction, times(1)).withdraw(account, BigDecimal.TEN);
        verify(executeTransaction, never()).deposit(account, BigDecimal.TEN);
    }
}
