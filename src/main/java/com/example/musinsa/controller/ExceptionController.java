package com.example.musinsa.controller;

import com.example.musinsa.controller.dto.ExceptionResponseDto;
import com.example.musinsa.service.exception.ParentCategoryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ParentCategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponseDto failToCreateChildCategory(ParentCategoryNotFoundException exception) {
        return new ExceptionResponseDto(exception.getMessage());
    }
}
