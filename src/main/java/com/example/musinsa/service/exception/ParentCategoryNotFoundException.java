package com.example.musinsa.service.exception;

public class ParentCategoryNotFoundException extends ApiFailResponseException {
    public ParentCategoryNotFoundException(String message) {
        super(message);
    }
}
