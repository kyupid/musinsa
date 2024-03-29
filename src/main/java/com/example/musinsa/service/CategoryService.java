package com.example.musinsa.service;

import com.example.musinsa.controller.dto.CategoryCreateRequestDto;
import com.example.musinsa.controller.dto.CategoryEditNameRequestDto;
import com.example.musinsa.controller.dto.CategoryResponseDto;
import com.example.musinsa.dao.CategoryDao;
import com.example.musinsa.service.exception.CategoryNotUpdatedException;
import com.example.musinsa.service.exception.ParentCategoryNotFoundException;
import com.example.musinsa.vo.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@EnableCaching
public class CategoryService {

    private final CategoryDao categoryDao;

    private static final String DELIMITER = "|";
    private static final String FIRST_CODE = "A";
    private static final Integer EXTRA_LEVEL = 1;
    private static final Integer ROOT_LEVEL = 0;

    public static final String CACHE_KEY_FOR_ALL_CATEGORY = "all";

    @CachePut(key = "#root.target.CACHE_KEY_FOR_ALL_CATEGORY", value = "categories")
    public CategoryResponseDto createCategory(CategoryCreateRequestDto request) {
        Category category = request.getParentId() == null ? getCreatingRootCategory(request) : getCreatingChildCategory(request);
        categoryDao.createCategory(category);
        return new CategoryResponseDto(categoryDao.findAll());
    }

    private Category getCreatingRootCategory(CategoryCreateRequestDto request) {
        String lastInsertCode = categoryDao.findLastInsertCodeByRootLevel(ROOT_LEVEL);
        Category category;
        if (StringUtils.isEmpty(lastInsertCode)) {
            category = Category.builder()
                    .name(request.getName())
                    .branch(FIRST_CODE)
                    .code(FIRST_CODE)
                    .level(ROOT_LEVEL)
                    .build();
        } else {
            category = Category.builder()
                    .name(request.getName())
                    .branch(nextCode(lastInsertCode))
                    .code(nextCode(lastInsertCode))
                    .level(ROOT_LEVEL)
                    .build();
        }
        return category;
    }

    private Category getCreatingChildCategory(CategoryCreateRequestDto request) {
        boolean exists = categoryDao.existsById(request.getParentId());
        if (!exists) {
            throw new ParentCategoryNotFoundException("입력하신 parentId는 존재하지 않습니다.");
        }

        Category parentCategory = categoryDao.findParentByParentId(request.getParentId());
        return Category.builder()
                .name(request.getName())
                .branch(parentCategory.getBranch())
                .code(getNewCode(parentCategory))
                .level(parentCategory.getLevel() + EXTRA_LEVEL)
                .build();
    }

    private String getNewCode(Category parentCategory) {
        // lastInsertCode는 parentCategory를 이용해 parentCategory.getLevel()의 +1을 해준다
        String lastInsertCode = categoryDao.findLastInsertCodeByBranchAndLevel(parentCategory.getBranch(), parentCategory.getLevel() + EXTRA_LEVEL);
        return lastInsertCode == null ? parentCategory.getCode() + DELIMITER + FIRST_CODE : nextCode(lastInsertCode);
    }

    private String nextCode(String lastCode) {
        String nextCode;
        boolean hasDelimiter = lastCode.contains(DELIMITER);
        if (hasDelimiter) {
            String[] splitCode = lastCode.split("\\" + DELIMITER);
            String lastSplitCode = splitCode[splitCode.length - 1];
            String nextAlphabet = nextAlphabet(lastSplitCode);
            splitCode[splitCode.length - 1] = nextAlphabet;
            nextCode = String.join(DELIMITER, splitCode);
        } else {
            nextCode = nextAlphabet(lastCode);
        }
        log.info("next code: {}", nextCode);
        return nextCode;
    }

    private String nextAlphabet(String source) {
        int length = source.length();
        char lastChar = source.charAt(length - 1);
        if (lastChar == 'Z') {
            if (length == 1) {
                return "AA";
            }
            source = nextAlphabet(source.substring(0, length - 1));
            source += "A";
            return source;
        }
        return source.substring(0, length - 1) + (char) (lastChar + 1);
    }

    @Cacheable(key = "#root.target.CACHE_KEY_FOR_ALL_CATEGORY", value = "categories")
    public CategoryResponseDto getAllCategories() {
        List<Category> categories = categoryDao.findAll();
        return new CategoryResponseDto(categories);
    }

    public CategoryResponseDto getChildCategoriesOfSelectedCategory(Integer id) {
        Category rootParentCategory = categoryDao.findById(id);
        String startSearchCode = rootParentCategory.getCode() + DELIMITER;
        List<Category> categories = categoryDao.findChildCategoriesOfSelectedCategory(rootParentCategory.getBranch(), startSearchCode);
        categories.add(0, rootParentCategory);
        return new CategoryResponseDto(categories);
    }

    @CachePut(key = "#root.target.CACHE_KEY_FOR_ALL_CATEGORY", value = "categories")
    public CategoryResponseDto deleteCategories(Integer id) {
        // 자기 자신과 하위 카테고리들 같이삭제
        Category rootParentCategory = categoryDao.findById(id);
        categoryDao.deleteCategory(id);
        categoryDao.deleteCategoriesOfSelectedCategory(rootParentCategory.getBranch(), rootParentCategory.getCode() + DELIMITER);
        return new CategoryResponseDto(categoryDao.findAll());
    }

    public int editCategoryName(CategoryEditNameRequestDto request) {
        int queryResult = categoryDao.updateCategoryName(request.getId(), request.getNewName());
        if (queryResult != 1) {
            throw new CategoryNotUpdatedException("요청 카테고리 id에 대한 정보가 없어 업데이트에 실패하였습니다.");
        }
        return queryResult;
    }
}
