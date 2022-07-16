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
    private static final String TEST_CATEGORY_NAME = "test_category";

    private CategoryCreateRequestDto rootCategoryCreateRequest(String testName) {
        CategoryCreateRequestDto categoryDTO = new CategoryCreateRequestDto(null, testName);
        return categoryDTO;
    }

    private CategoryCreateRequestDto childCategoryCreateRequest(Integer parentId, String testName) {
        CategoryCreateRequestDto categoryDTO = new CategoryCreateRequestDto(parentId, testName);
        return categoryDTO;
    }

    private Integer saveFirstRootCategoryStub() {
        Category category = Category.builder()
                .name("test_root_category")
                .branch(FIRST_CODE)
                .code(FIRST_CODE)
                .level(ROOT_LEVEL)
                .build();
        categoryDao.createCategory(category);
        return category.getId();
    }

    @Test
    void 첫_루트_카테고리_저장하는_테스트() {
        //given
        CategoryCreateRequestDto request = rootCategoryCreateRequest(TEST_CATEGORY_NAME);
        Integer id = categoryService.createCategory(request);

        //when
        Category category = categoryDao.findById(id);

        log.info("Category: {}", category);

        //then
        assertThat(category.getCode()).isEqualTo(FIRST_CODE);
    }

    @Test
    void 두번째_루트_카테고리_저장하는_테스트() {
        //given
        saveFirstRootCategoryStub();
        CategoryCreateRequestDto request = rootCategoryCreateRequest(TEST_CATEGORY_NAME);
        Integer id = categoryService.createCategory(request);

        //when
        Category category = categoryDao.findById(id);
        log.info("Category: {}", category);

        //then
        assertThat(category.getCode()).isEqualTo("B");
    }

    @Test
    void 첫번째_루트_카테고리의_자식_카테고리_생성_테스트() {
        //given
        Integer firstRootCategoryId = saveFirstRootCategoryStub();
        log.info("Category: {}", firstRootCategoryId);

        CategoryCreateRequestDto request = childCategoryCreateRequest(firstRootCategoryId, TEST_CATEGORY_NAME);
        Integer id = categoryService.createCategory(request);

        //when
        Category category = categoryDao.findById(id);
        log.info("Category: {}", category);

        //then
        assertThat(category.getCode()).isEqualTo(FIRST_CODE + DELIMITER + FIRST_CODE);
    }

    @Test
    void 첫번째_루트_카테고리의_두번째_자식_카테고리_생성_테스트() {
        //given
        Integer firstRootCategoryId = saveFirstRootCategoryStub();
        CategoryCreateRequestDto childRequest = childCategoryCreateRequest(firstRootCategoryId, TEST_CATEGORY_NAME);
        categoryService.createCategory(childRequest);
        CategoryCreateRequestDto childRequest2 = childCategoryCreateRequest(firstRootCategoryId, TEST_CATEGORY_NAME);
        Integer id = categoryService.createCategory(childRequest2);

        //when
        Category category = categoryDao.findById(id);
        log.info("Category: {}", category);

        //then
        assertThat(category.getCode()).isEqualTo(FIRST_CODE + DELIMITER + "B");
    }

}
