package com.example.musinsa.service.exception;

public class ApiFailResponseException extends RuntimeException {
    public ApiFailResponseException(String message) {
        super(message);
    }
}
