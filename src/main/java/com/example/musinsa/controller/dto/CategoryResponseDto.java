package com.example.musinsa.controller.dto;

import com.example.musinsa.vo.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryResponseDto {
    private List<Category> categories;
}
