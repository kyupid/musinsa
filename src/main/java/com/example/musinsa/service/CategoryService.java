package com.example.musinsa.service;

import com.example.musinsa.controller.dto.CategoryCreateRequestDto;
import com.example.musinsa.dao.CategoryDao;
import com.example.musinsa.vo.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryDao categoryDao;

    private static final String DELIMITER = "|";
    private static final String FIRST_CODE = "A";
    private static final Integer EXTRA_LEVEL = 1;

    public void create(CategoryCreateRequestDto request) {
        if (request.getParentId() == null) {
            categoryDao.createRootCategory(request.getName());
        } else { // 부모가 있을 경우
            Category parentCategory = categoryDao.findParentByParentId(request.getParentId());
            String lastInsertCode = categoryDao.findLastInsertCode(request.getParentId(), parentCategory.getLevel());
            String extraCode;
            if (lastInsertCode == null) { // 그 부모에 대한 첫 카테고리
                extraCode = DELIMITER + FIRST_CODE;
            } else { // 그 부모에 대한 첫 카테고리가 아니라면
                extraCode = DELIMITER + nextCode(parentCategory.getCode());
            }
            String newCode = parentCategory.getCode() + extraCode;
            categoryDao.createChildCategory(request.getName(), newCode, parentCategory.getLevel() + EXTRA_LEVEL);
        }
    }
}
