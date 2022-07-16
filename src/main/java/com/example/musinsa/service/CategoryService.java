package com.example.musinsa.service;

import com.example.musinsa.controller.dto.CategoryCreateRequestDto;
import com.example.musinsa.dao.CategoryDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryDao categoryDao;

    public void create(CategoryCreateRequestDto request) {
        if (request.getRootId() == null) {
            categoryDao.createRootCategory(request.getName());
        } else {

        }
    }
}
