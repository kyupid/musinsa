package com.example.musinsa.controller;

import com.example.musinsa.controller.dto.CategoryCreateRequestDto;
import com.example.musinsa.controller.dto.CategoryEditNameRequestDto;
import com.example.musinsa.controller.dto.CategoryResponseDto;
import com.example.musinsa.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@EnableCaching
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryApiController {

    private final CategoryService categoryService;
    public static final String CACHE_KEY_FOR_ALL_CATEGORY = "all";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer createCategory(@RequestBody @Valid CategoryCreateRequestDto request) {
        return categoryService.createCategory(request);
    }

    @Cacheable(key = "#root.target.CACHE_KEY_FOR_ALL_CATEGORY", value = "categories")
    @GetMapping
    public CategoryResponseDto allCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{selectedId}")
    public CategoryResponseDto childCategoriesOfSelectedCategory(@PathVariable Integer selectedId) {
        return categoryService.getChildCategoriesOfSelectedCategory(selectedId);
    }

    @CachePut(key = "#root.target.CACHE_KEY_FOR_ALL_CATEGORY", value = "categories")
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
