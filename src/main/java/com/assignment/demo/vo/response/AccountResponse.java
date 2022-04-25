package com.assignment.demo.vo.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
public class AccountResponse {
    private UUID id;
    private BigDecimal balance;
    private String name;
    private CustomerResponse customer;
}
