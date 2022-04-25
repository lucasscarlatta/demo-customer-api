package com.assignment.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiSubErrorDTO {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;
}
