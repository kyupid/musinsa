package com.example.musinsa.service;

import com.example.musinsa.controller.dto.CategoryCreateRequestDto;
import com.example.musinsa.dao.CategoryDao;
import com.example.musinsa.vo.Category;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CategoryServiceTest {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryDao categoryDao;

    private static final String DELIMITER = "|";
    private static final String FIRST_CODE = "A";
    private static final Integer EXTRA_LEVEL = 1;
    private static final Integer ROOT_LEVEL = 0;

    private CategoryCreateRequestDto rootCategoryCreateRequest(String testName) {
        CategoryCreateRequestDto categoryDTO = new CategoryCreateRequestDto(null, testName);
        return categoryDTO;
    }

    private CategoryCreateRequestDto childCategoryCreateRequest(Integer parentId, String testName) {
        CategoryCreateRequestDto categoryDTO = new CategoryCreateRequestDto(parentId, testName);
        return categoryDTO;
    }

    private void saveFirstRootCategoryStub() {
        Category category = Category.builder()
                .name("test_root_category")
                .branch(FIRST_CODE)
                .code(FIRST_CODE)
                .level(ROOT_LEVEL)
                .build();
        categoryDao.createCategory(category);
    }

    @Test
    void 첫_루트_카테고리_저장하는_테스트() {
        final String testCategoryName = "test_root_category_2";

        //given
        CategoryCreateRequestDto request = rootCategoryCreateRequest(testCategoryName);
        Integer id = categoryService.createCategory(request);

        //when
        Category category = categoryDao.findById(id);

        log.info("Category: {}", category);

        //then
        assertThat(category.getCode()).isEqualTo(FIRST_CODE);
    }

    @Test
    void 두번째_루트_카테고리_저장하는_테스트() {
        final String testCategoryName = "test_root_category";

        //given
        saveFirstRootCategoryStub();
        CategoryCreateRequestDto request = rootCategoryCreateRequest(testCategoryName);
        Integer id = categoryService.createCategory(request);

        //when
        Category category = categoryDao.findById(id);
        log.info("Category: {}", category);

        //then
        assertThat(category.getCode()).isEqualTo("B");
    }
}
