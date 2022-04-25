package com.assignment.demo.it.service;

import com.assignment.demo.domain.Account;
import com.assignment.demo.domain.User;
import com.assignment.demo.domain.enums.TransactionType;
import com.assignment.demo.exception.NoContentException;
import com.assignment.demo.exception.NotFoundException;
import com.assignment.demo.it.AbstractServiceIT;
import com.assignment.demo.repository.AccountRepository;
import com.assignment.demo.repository.TransactionRepository;
import com.assignment.demo.repository.UserRepository;
import com.assignment.demo.service.AccountService;
import com.assignment.demo.service.TransactionService;
import com.assignment.demo.vo.request.AccountRequest;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransactionServiceIT extends AbstractServiceIT {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveAndReadTransaction() {
        var initialBalance = BigDecimal.TEN;
        var amount = new BigDecimal("100.10");

        var account = createAccount(initialBalance);
        transactionService.transaction(account.getId(), TransactionType.DEPOSIT, amount);

        var transactions = transactionService.getAllTransactions();

        assertNotNull(transactions);
        assertFalse(transactions.isEmpty());
        assertAll("Transaction list", () -> {
            assertEquals(transactions.size(), 1);
            assertAll("First Transaction", () -> {
                var firstTransaction = transactions.get(0);
                assertEquals(initialBalance.add(amount), firstTransaction.getAccount().getBalance());
            });
        });
    }

    @Test
    public void saveMultipleTransactions() {
        var initialBalance = BigDecimal.ZERO;
        var firstDeposit = new BigDecimal("100.00");
        var secondDeposit = new BigDecimal("200");
        var firstWithdraw = new BigDecimal("20");
        var secondWithdraw = new BigDecimal("30");

        var account = createAccount(initialBalance);
        transactionService.transaction(account.getId(), TransactionType.DEPOSIT, firstDeposit);
        transactionService.transaction(account.getId(), TransactionType.WITHDRAW, firstWithdraw);
        transactionService.transaction(account.getId(), TransactionType.DEPOSIT, secondDeposit);
        transactionService.transaction(account.getId(), TransactionType.WITHDRAW, secondWithdraw);

        var transactions = transactionService.getAllTransactions();

        assertNotNull(transactions);
        assertFalse(transactions.isEmpty());
        assertAll("All transaction from same account", () -> {
            assertEquals(transactions.size(), 1);
            assertAll("First Transaction", () -> {
                var firstTransaction = transactions.get(0);
                var balance = initialBalance.add(firstDeposit).add(secondDeposit).subtract(firstWithdraw)
                        .subtract(secondWithdraw);
                assertEquals(balance, firstTransaction.getAccount().getBalance());
            });
        });
    }

    @Test
    public void findTransactionByAccountId() {
        var initial = new BigDecimal("100.00");
        var account = createAccount(initial);

        var transactionResponse = transactionService.getTransactionsByAccountId(account.getId());

        assertNotNull(transactionResponse);
        assertAll("Transaction list", () -> {
            assertEquals(transactionResponse.getTransactions().size(), 1);
            assertAll("First Account", () -> {
                var firstTransaction = transactionResponse.getTransactions().get(0);
                assertEquals(initial, firstTransaction.getAmount());
                assertEquals(account.getBalance(), firstTransaction.getAccount().getBalance());
                assertEquals(TransactionType.DEPOSIT, firstTransaction.getType());
            });
        });
    }

    @Test
    public void findTransactionByAccountIdNotFound() {
        var account = createAccount(BigDecimal.ZERO);

        var thrown = assertThrows(NoContentException.class, () -> transactionService
                .getTransactionsByAccountId(account.getId()));

        assertEquals(String.format("Transactions no content for account id. [AccountId: %s]", account.getId()),
                thrown.getMessage());
    }

    @After
    public void after() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();
    }

    private Account createAccount(BigDecimal initialBalance) {
        var name = "Test account";
        var customer = createUser();
        var accountRequest = new AccountRequest();
        accountRequest.setCustomerId(customer.getCustomerId());
        accountRequest.setInitial(initialBalance);
        accountRequest.setName(name);

        return accountService.createAccount(accountRequest);
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
