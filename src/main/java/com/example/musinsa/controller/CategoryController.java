package com.example.musinsa.controller;

import com.example.musinsa.controller.dto.CategoryResponseDto;
import com.example.musinsa.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 과제 확인을 위한 프론트 페이지입니다
 */
@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public String goToCategory(Model model) {
        CategoryResponseDto allCategories = categoryService.getAllCategories();
        model.addAttribute("categories", allCategories);
        return "category";
    }

    @GetMapping("/{categoryId}")
    public String goToCategory(@PathVariable Integer categoryId, Model model) {
        CategoryResponseDto allCategories = categoryService.getChildCategoriesOfSelectedCategory(categoryId);
        model.addAttribute("categories", allCategories);
        return "category";
    }
}
