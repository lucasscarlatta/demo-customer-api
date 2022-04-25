package com.assignment.demo.service;

import com.assignment.demo.domain.enums.TransactionType;
import com.assignment.demo.vo.response.TransactionResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface TransactionService {

    void transaction(UUID accountId, TransactionType transactionType, BigDecimal deposit);

    List<TransactionResponse> getAllTransactions();

    TransactionResponse getTransactionsByAccountId(UUID accountId);
}
