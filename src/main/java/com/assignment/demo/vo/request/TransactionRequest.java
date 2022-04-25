package com.assignment.demo.vo.request;

import com.assignment.demo.domain.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class TransactionRequest {

    @NotNull
    private UUID accountId;

    @NotNull
    private TransactionType transactionType;

    @Min(value = 0)
    private BigDecimal amount;
}
