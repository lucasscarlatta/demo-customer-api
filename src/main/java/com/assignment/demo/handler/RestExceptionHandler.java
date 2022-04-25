package com.assignment.demo.handler;

import com.assignment.demo.dto.ApiSubErrorDTO;
import com.assignment.demo.exception.NoContentException;
import com.assignment.demo.exception.NotFoundException;
import com.assignment.demo.vo.response.ApiErrorResponse;
import io.micrometer.core.lang.NonNullApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j(topic = "Exceptions")
@NonNullApi
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        log.error("malformed_json_error", ex);

        return buildResponseEntity(new ApiErrorResponse(BAD_REQUEST, "malformed json error", ex));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        log.error(ex.getMessage(), ex);
        var errors = ex.getBindingResult().getFieldErrors();
        var subErros = errors.stream().map(this::subErros).collect(Collectors.toList());
        return buildResponseEntity(new ApiErrorResponse(PRECONDITION_FAILED, ex.getMessage(), ex, subErros));
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    @ResponseStatus(value = NOT_FOUND)
    public ApiErrorResponse handleNotFoundException(NotFoundException ex, WebRequest request) {
        log.error("Not found", ex);

        return new ApiErrorResponse(NOT_FOUND, ex.getMessage(), ex);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(value = PRECONDITION_FAILED)
    public ApiErrorResponse handleConstraintViolation(Exception ex, WebRequest request) {
        var constraintViolations = ((ConstraintViolationException) ex).getConstraintViolations();
        var subErros = constraintViolations.stream().map(this::subErros).collect(Collectors.toList());
        return new ApiErrorResponse(PRECONDITION_FAILED, ex.getMessage(), ex, subErros);
    }

    @ExceptionHandler(NoContentException.class)
    @ResponseBody
    @ResponseStatus(value = NO_CONTENT)
    public void handleNoContentException(NoContentException ex, WebRequest request) {
        log.info(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(value = INTERNAL_SERVER_ERROR)
    public ApiErrorResponse handleOtherException(Exception ex, WebRequest request) {
        log.error("Unexpected error", ex);

        return new ApiErrorResponse("Unexpected error", ex);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiErrorResponse apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    private ApiSubErrorDTO subErros(ConstraintViolation<?> constraint) {
        return ApiSubErrorDTO.builder().object(constraint.getLeafBean().toString()).message(constraint.getMessage())
                .field(constraint.getPropertyPath().toString()).rejectedValue(constraint.getInvalidValue()).build();
    }

    private ApiSubErrorDTO subErros(FieldError fieldError) {
        return ApiSubErrorDTO.builder().object(fieldError.getObjectName()).message(fieldError.getDefaultMessage())
                .field(fieldError.getField()).rejectedValue(fieldError.getRejectedValue()).build();
    }
}
