package com.assignment.demo.vo.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class AccountRequest {

    @NotNull
    private UUID customerId;

    @NotBlank
    private String name;

    @Min(value = 0)
    private BigDecimal initial = BigDecimal.ZERO;
}
