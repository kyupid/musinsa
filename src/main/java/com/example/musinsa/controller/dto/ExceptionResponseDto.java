package com.example.musinsa.controller.dto;

import lombok.Getter;

@Getter
public class ExceptionResponseDto {
    private String message;

    public ExceptionResponseDto(String message) {
        this.message = message;
    }
}
