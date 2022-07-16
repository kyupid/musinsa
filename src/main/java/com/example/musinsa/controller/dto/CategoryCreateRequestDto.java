package com.example.musinsa.controller.dto;

import lombok.Getter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

@Getter
public class CategoryCreateRequestDto {

    @Nullable
    private Integer parentId;

    @NotNull
    private String name;
}
