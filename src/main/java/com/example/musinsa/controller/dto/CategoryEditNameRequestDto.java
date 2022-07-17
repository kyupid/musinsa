package com.example.musinsa.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEditNameRequestDto {
    private Integer id;

    @NotEmpty
    private String newName;
}
