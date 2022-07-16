package com.example.musinsa.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEditNameRequestDto {
    private Integer id;
    private String newName;
}
