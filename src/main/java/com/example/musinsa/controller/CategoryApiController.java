package com.example.musinsa.controller;

import com.example.musinsa.controller.dto.CategoryCreateRequestDto;
import com.example.musinsa.controller.dto.CategoryEditNameRequestDto;
import com.example.musinsa.controller.dto.CategoryResponseDto;
import com.example.musinsa.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryApiController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer createCategory(@RequestBody @Valid CategoryCreateRequestDto request) {
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Integer selectedId) {
        categoryService.deleteCategories(selectedId);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void editCategoryName(@RequestBody @Valid CategoryEditNameRequestDto request) {
        categoryService.editCategoryName(request);
    }
}
