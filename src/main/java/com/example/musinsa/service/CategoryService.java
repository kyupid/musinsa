package com.example.musinsa.service;

import com.example.musinsa.controller.dto.CategoryCreateRequestDto;
import com.example.musinsa.controller.dto.CategoryResponseDto;
import com.example.musinsa.dao.CategoryDao;
import com.example.musinsa.vo.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryService {

    private final CategoryDao categoryDao;

    private static final String DELIMITER = "|";
    private static final String FIRST_CODE = "A";
    private static final Integer EXTRA_LEVEL = 1;
    private static final Integer ROOT_LEVEL = 0;

    public Integer createCategory(CategoryCreateRequestDto request) {
        if (request.getParentId() == null) {
            // 부모가 없을 경우
            // 1. level이 0인것중에 order by DESC limit 1 한다
            // 2. 거기에서 next alphabet 한다
            // 3. code = code + next alphabet
            String lastCode = categoryDao.findLastInsertCodeByRootLevel(ROOT_LEVEL);
            log.info("lastCode: {}", lastCode);
            Category category;
            if (lastCode == null) {
                category = Category.builder()
                        .name(request.getName())
                        .branch(FIRST_CODE)
                        .code(FIRST_CODE)
                        .level(ROOT_LEVEL)
                        .build();
            } else {
                category = Category.builder()
                        .name(request.getName())
                        .branch(nextCode(lastCode))
                        .code(nextCode(lastCode))
                        .level(ROOT_LEVEL)
                        .build();
            }
            categoryDao.createCategory(category);
            log.info("categoory.getId(): {}", category.getId());
            return category.getId();
        } else { // 부모가 있을 경우
            Category parentCategory = categoryDao.findParentByParentId(request.getParentId());

            // lastInsertCode는 request의 level이 필ㅇ하기때문에 parentCategory.getLevel()의 +1을 해준다
            String lastInsertCode = categoryDao.findLastInsertCodeByBranchAndLevel(parentCategory.getBranch(), parentCategory.getLevel() + EXTRA_LEVEL);
            log.info("부모가있을경우 lastInsertCode는: {}", lastInsertCode);
            String extraCode;
            String newCode;
            if (lastInsertCode == null) { // 그 부모카테고리에 대한 첫 카테고리
                log.info("부모카테고리가 있고 lastInsertCode가 null입니다");
                extraCode = DELIMITER + FIRST_CODE;
                log.info("부모가있을ㅇ경우의 extraCode는: {}", extraCode);
                newCode = parentCategory.getCode() + extraCode;
            } else { // 그 부모카테고리에 대한 카테고리가 이미 존재한다면
                log.info("부모카테고리가 있고 lastInsertCode가 null이 아닙니니다");
                extraCode = nextCode(lastInsertCode);
                newCode = extraCode;
            }
            log.info("부모가있을ㅇ경우의 newCode는: {}", newCode);
            Category category = Category.builder()
                    .name(request.getName())
                    .branch(parentCategory.getBranch())
                    .code(newCode)
                    .level(parentCategory.getLevel() + EXTRA_LEVEL)
                    .build();
            categoryDao.createCategory(category);
            return category.getId();
        }
    }

    private String nextCode(String lastCode) {
        log.info("lastCode: {}", lastCode);
        String nextCode;
        boolean hasDelimiter = lastCode.contains(DELIMITER);
        log.info("hasDelimiter: {}", hasDelimiter);
        if (hasDelimiter) {
            String[] splitCode = lastCode.split("\\" + DELIMITER);
            String lastSplitCode = splitCode[splitCode.length - 1];
            String nextAlphabet = nextAlphabet(lastSplitCode);
            splitCode[splitCode.length - 1] = nextAlphabet;
            nextCode = String.join(DELIMITER, splitCode);
            log.info("delimiter가 있고, nextcode는: {}", nextCode);
        } else {
            nextCode = nextAlphabet(lastCode);
        }
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

    public CategoryResponseDto getAllCategories() {
        List<Category> categories = categoryDao.findAll();
        return new CategoryResponseDto(categories);
    }
}
