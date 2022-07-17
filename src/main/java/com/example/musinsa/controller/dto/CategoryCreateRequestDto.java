package com.example.musinsa.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateRequestDto {

    @Nullable
    private Integer parentId;

    @NotEmpty
    private String name;
}
