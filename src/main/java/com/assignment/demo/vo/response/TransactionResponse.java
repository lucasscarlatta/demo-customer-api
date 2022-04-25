package com.assignment.demo.vo.response;

import com.assignment.demo.domain.Transaction;
import com.assignment.demo.domain.User;
import com.assignment.demo.domain.enums.TransactionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class TransactionResponse {

    private AccountResponse account;
    private List<Transaction> transactions;
}
