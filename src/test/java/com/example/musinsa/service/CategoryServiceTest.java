package com.example.musinsa.service;

import com.example.musinsa.controller.dto.CategoryCreateRequestDto;
import com.example.musinsa.controller.dto.CategoryEditNameRequestDto;
import com.example.musinsa.dao.CategoryDao;
import com.example.musinsa.vo.Category;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    private Integer saveTestFirstRootCategory() {
        Category category = Category.builder()
                .name("test_root_category")
                .branch(FIRST_CODE)
                .code(FIRST_CODE)
                .level(ROOT_LEVEL)
                .build();
        categoryDao.createCategory(category);
        return category.getId();
    }

    private List<Category> saveTestCategories() {
        // 루트 level 0
        for (int i = 0; i < 10; i++) {
            categoryService.createCategory(rootCategoryCreateRequest(TEST_CATEGORY_NAME));
        }

        // level 1
        for (int i = 0; i < 10; i++) {
            categoryService.createCategory(childCategoryCreateRequest(i + 1, TEST_CATEGORY_NAME));
        }

        // level 2
        int idAfterSaving = 0;
        for (int i = 0; i < 10; i++) {
            idAfterSaving = categoryService.createCategory(childCategoryCreateRequest(i + 10, TEST_CATEGORY_NAME));
        }

        // 마지막 생성한 카테고리에 대한 자식 -> 자식 -> 자식 -> ... 10개 생성
        for (int i = 0; i < 10; i++) {
            idAfterSaving = categoryService.createCategory(childCategoryCreateRequest(idAfterSaving, TEST_CATEGORY_NAME));
        }

        return categoryDao.findAll();
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
        saveTestFirstRootCategory();
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
        Integer firstRootCategoryId = saveTestFirstRootCategory();
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
        Integer firstRootCategoryId = saveTestFirstRootCategory();
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

    @Test
    void 첫_루트_카테고리_첫_자식_첫_자식_생성_테스트() {
        //given
        Integer firstRootCategoryId = saveTestFirstRootCategory();
        CategoryCreateRequestDto childRequest = childCategoryCreateRequest(firstRootCategoryId, TEST_CATEGORY_NAME);
        Integer rootChildId = categoryService.createCategory(childRequest);
        CategoryCreateRequestDto childChildRequest = childCategoryCreateRequest(rootChildId, TEST_CATEGORY_NAME);
        Integer id = categoryService.createCategory(childChildRequest);

        //when
        Category category = categoryDao.findById(id);
        log.info("Category: {}", category);

        //then
        assertThat(category.getCode()).isEqualTo(FIRST_CODE + DELIMITER + FIRST_CODE + DELIMITER + FIRST_CODE);
    }

    @Test
    void 테스트_카테고리_저장_테스트() {
        //given
        saveTestCategories();

        //when
        List<Category> categories = categoryDao.findAll();
        log.info("categories: {}", categories);

        //then
        assertThat(categories.size()).isEqualTo(40);
        int countMoreThanLevelTwo = categories.stream()
                .filter(c -> c.getLevel() > 12)
                .collect(Collectors.toList())
                .size();
        assertThat(countMoreThanLevelTwo).isEqualTo(0);
    }

    @Test
    void 자식_카테고리가_없는_카테고리_삭제_테스트() {
        //given
        Integer id = saveTestFirstRootCategory();

        //when
        categoryService.deleteCategories(id);

        //then
        Category category = categoryDao.findById(id);

        assertThat(category).isNull();
    }

    @Test
    void 자식_카테고리가_있는_카테고리_삭제_테스트() {
        //given
        saveTestCategories();

        //when
        categoryService.deleteCategories(30);

        //then
        List<Category> categoriesAfterDeleting = categoryDao.findAll();

        assertThat(categoriesAfterDeleting.size()).isEqualTo(29);
        int countMoreThanLevelTwo = categoriesAfterDeleting.stream()
                .filter(c -> c.getLevel() > 2)
                .collect(Collectors.toList())
                .size();
        assertThat(countMoreThanLevelTwo).isEqualTo(0);
    }

    @Test
    void 마지막_자식_카테고리_삭제_테스트() {
        //given
        List<Category> categories = saveTestCategories();

        //when
        Integer lastChildId = categories.size();
        categoryService.deleteCategories(lastChildId);

        //then
        List<Category> categoriesAfterDeleting = categoryDao.findAll();

        assertThat(categoriesAfterDeleting.size()).isEqualTo(39);
    }

    @Test
    void 카테고리_이름_수정_테스트() {
        //given
        saveTestFirstRootCategory();
        final String newName = "new category name";
        CategoryEditNameRequestDto request = new CategoryEditNameRequestDto(1, newName);

        //when
        int isUpdated = categoryService.editCategoryName(request);

        //then
        Category category = categoryDao.findById(request.getId());

        assertThat(isUpdated).isOne();
        assertThat(category.getName()).isEqualTo(newName);
    }
}
