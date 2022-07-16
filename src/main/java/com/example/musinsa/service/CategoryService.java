package com.example.musinsa.service;

import com.example.musinsa.dao.CategoryDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryDao categoryDao;
}
