package com.example.musinsa.controller;

import com.example.musinsa.controller.dto.CategoryResponseDto;
import com.example.musinsa.dao.CategoryDao;
import com.example.musinsa.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 과제 확인을 위한 프론트 페이지입니다
 */
@Controller
@RequiredArgsConstructor
@EnableCaching
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryDao categoryDao;

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

    /**
     * 테스트 위한 데이터 초기화 api
     */
    @CacheEvict(value = "categories", key = "'all'")
    @GetMapping("/api/dropTable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void dropTable() {
        categoryDao.dropTable();
    }
}
