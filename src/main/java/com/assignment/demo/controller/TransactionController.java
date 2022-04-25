package com.assignment.demo.controller;

import com.assignment.demo.service.TransactionService;
import com.assignment.demo.vo.request.TransactionRequest;
import com.assignment.demo.vo.response.TransactionResponse;
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
@RequestMapping("v1/transactions")
@Validated
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    void transaction(@RequestBody @Valid @NotNull TransactionRequest transactionRequest) {
        var stopWatch = new Slf4JStopWatch(String.format("Execute transaction for account [TransactionType: %s, AccountId: %s]",
                transactionRequest.getTransactionType(), transactionRequest.getAccountId()));
        try {
            transactionService.transaction(transactionRequest.getAccountId(), transactionRequest.getTransactionType(),
                    transactionRequest.getAmount());
        } finally {
            stopWatch.stop();
        }
    }

    @GetMapping
    List<TransactionResponse> getAllTransactions() {
        var stopWatch = new Slf4JStopWatch("Get all transactions");
        try {
            return transactionService.getAllTransactions();
        } finally {
            stopWatch.stop();
        }
    }

    @GetMapping("accounts/{accountId}")
    TransactionResponse getTransactionsByAccountId(@PathVariable UUID accountId) {
        var stopWatch = new Slf4JStopWatch(String.format("Get transactions by accountId [AccountId: %s]", accountId));
        try {
            return transactionService.getTransactionsByAccountId(accountId);
        } finally {
            stopWatch.stop();
        }
    }
}



