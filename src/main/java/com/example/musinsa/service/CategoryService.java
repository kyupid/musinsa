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
    private static final Integer ROOT_LEVEL = 0;

    public void create(CategoryCreateRequestDto request) {
        if (request.getParentId() == null) {
            // 부모가 없을 경우
            // 1. level이 0인것중에 order by DESC limit 1 한다
            // 2. 거기에서 next alphabet 한다
            // 3. code = code + next alphabet
            String lastCode = categoryDao.findLastCodeByLevel(ROOT_LEVEL);
            Category category = Category.builder()
                    .name(request.getName())
                    .branch(nextCode(lastCode))
                    .code(nextCode(lastCode))
                    .level(ROOT_LEVEL)
                    .build();
            categoryDao.createRootCategory(category);
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
