package com.example.musinsa.service.exception;

public class CategoryNotUpdatedException extends ApiFailResponseException {
    public CategoryNotUpdatedException(String message) {
        super(message);
    }
}
