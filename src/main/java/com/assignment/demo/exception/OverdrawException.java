package com.assignment.demo.exception;

public class OverdrawException extends RuntimeException {

    public OverdrawException(String message) {
        super(message);
    }
}
