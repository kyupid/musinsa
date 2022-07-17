package com.example.musinsa.controller;

import com.example.musinsa.controller.dto.CategoryCreateRequestDto;
import com.example.musinsa.controller.dto.CategoryEditNameRequestDto;
import com.example.musinsa.controller.dto.CategoryResponseDto;
import com.example.musinsa.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryApiController {

    private final CategoryService categoryService;

    @PostMapping
    public Integer createCategory(@RequestBody CategoryCreateRequestDto request) {
        return categoryService.createCategory(request);
    }

    @GetMapping
    public CategoryResponseDto allCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{selectedId}")
    public CategoryResponseDto childCategoriesOfSelectedCategory(@PathVariable Integer selectedId) {
        return categoryService.getChildCategoriesOfSelectedCategory(selectedId);
    }

    @DeleteMapping("/{selectedId}")
    public void deleteCategory(@PathVariable Integer selectedId) {
        categoryService.deleteCategories(selectedId);
    }

    @PatchMapping
    public void editCategoryName(@RequestBody CategoryEditNameRequestDto request) {
        categoryService.editCategoryName(request);
    }
}
