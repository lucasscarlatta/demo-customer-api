package com.assignment.demo.it.controller;

import com.assignment.demo.controller.TransactionController;
import com.assignment.demo.domain.Account;
import com.assignment.demo.domain.Transaction;
import com.assignment.demo.domain.enums.TransactionType;
import com.assignment.demo.exception.NoContentException;
import com.assignment.demo.it.AbstractControllerIT;
import com.assignment.demo.it.AbstractIT;
import com.assignment.demo.service.AccountService;
import com.assignment.demo.service.TransactionService;
import com.assignment.demo.service.impl.ExecuteTransaction;
import com.assignment.demo.vo.request.TransactionRequest;
import com.assignment.demo.vo.response.AccountResponse;
import com.assignment.demo.vo.response.CustomerResponse;
import com.assignment.demo.vo.response.TransactionResponse;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
public class TransactionControllerIT extends AbstractControllerIT {

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private AccountService accountService;

    @MockBean
    private ExecuteTransaction executeTransaction;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFindAll() throws Exception {
        var customer = CustomerResponse.builder().name("Name").surname("Surname").build();
        var account = AccountResponse.builder().balance(new BigDecimal("100"))
                .customer(customer).build();

        var transaction = new Transaction();
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setAmount(new BigDecimal("100"));

        var transactionResponse = singletonList(TransactionResponse.builder()
                .account(account).transactions(singletonList(transaction))
                .build());

        when(transactionService.getAllTransactions()).thenReturn(transactionResponse);

        mockMvc.perform(get("/v1/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].account.balance", Matchers.is(100)))
                .andExpect(jsonPath("$[0].transactions", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].transactions[0].amount", Matchers.is(100)))
                .andExpect(jsonPath("$[0].transactions[0].type", Matchers.is(TransactionType.DEPOSIT.name())));
    }

    @Test
    public void testFindAllEmptyLst() throws Exception {
        when(transactionService.getAllTransactions()).thenThrow(NoContentException.class);

        mockMvc.perform(get("/v1/transactions"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testFindByAccountId() throws Exception {
        var accountId = UUID.randomUUID();
        var customer = CustomerResponse.builder().id(UUID.randomUUID()).build();

        var name = "Account test";
        var accountResponse = AccountResponse.builder()
                .id(accountId).balance(new BigDecimal("100")).customer(customer).name(name)
                .build();

        var transaction = new Transaction();
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setAmount(new BigDecimal("100"));

        var transactionResponse = TransactionResponse.builder()
                .account(accountResponse).transactions(singletonList(transaction))
                .build();

        var account = new Account();
        account.setId(accountId);

        when(accountService.getAccountById(accountId)).thenReturn(account);
        when(transactionService.getTransactionsByAccountId(accountId)).thenReturn(transactionResponse);

        mockMvc.perform(get(String.format("/v1/transactions/accounts/%s", accountId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactions", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.account.name", Matchers.is(name)))
                .andExpect(jsonPath("$.account.balance", Matchers.is(100)));
    }

    @Test
    public void testFindByAccountIdNotFound() throws Exception {
        var id = UUID.randomUUID();
        var account = new Account();
        account.setId(id);

        when(accountService.getAccountById(id)).thenReturn(account);
        when(transactionService.getTransactionsByAccountId(id)).thenThrow(NoContentException.class);

        mockMvc.perform(get(String.format("/v1/transactions/accounts/%s", id)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testExecuteDepositTransaction() throws Exception {
        var accountId = UUID.randomUUID();
        var account = new Account();
        account.setId(accountId);

        var deposit = new BigDecimal("100");
        var transactionRequest = new TransactionRequest();
        transactionRequest.setTransactionType(TransactionType.DEPOSIT);
        transactionRequest.setAmount(deposit);
        transactionRequest.setAccountId(accountId);

        when(accountService.getAccountById(accountId)).thenReturn(account);
        doNothing().when(executeTransaction).deposit(account, deposit);

        mockMvc.perform(post("/v1/transactions").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper(transactionRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testExecuteWithdrawTransaction() throws Exception {
        var accountId = UUID.randomUUID();
        var account = new Account();
        account.setId(accountId);
        account.setBalance(new BigDecimal("1000"));

        var withdraw = new BigDecimal("100");
        var transactionRequest = new TransactionRequest();
        transactionRequest.setTransactionType(TransactionType.WITHDRAW);
        transactionRequest.setAmount(withdraw);
        transactionRequest.setAccountId(accountId);

        when(accountService.getAccountById(accountId)).thenReturn(account);
        doNothing().when(executeTransaction).withdraw(account, withdraw);

        mockMvc.perform(post("/v1/transactions").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper(transactionRequest)))
                .andExpect(status().isCreated());
    }
}
