package com.assignment.demo.vo.response;

import com.assignment.demo.config.serializer.JsonDateTimeDeserializer;
import com.assignment.demo.config.serializer.JsonDateTimeSerializer;
import com.assignment.demo.dto.ApiSubErrorDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {

    @JsonDeserialize(using = JsonDateTimeDeserializer.class)
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private final LocalDateTime timestamp;
    private HttpStatus status;
    private String message;
    private String debugMessage;
    private List<ApiSubErrorDTO> subErrors;

    private ApiErrorResponse() {
        timestamp = LocalDateTime.now();
    }

    public ApiErrorResponse(String message, Throwable ex) {
        this();
        this.status = INTERNAL_SERVER_ERROR;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    public ApiErrorResponse(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    public ApiErrorResponse(HttpStatus status, String message, Throwable ex, List<ApiSubErrorDTO> subErrors) {
        this(status, message, ex);
        this.subErrors = subErrors;
    }
}