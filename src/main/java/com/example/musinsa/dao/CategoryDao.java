package com.example.musinsa.dao;

import com.example.musinsa.vo.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryDao {
    void createCategory(@Param("category") Category category);

    Category findById(Integer id);

    Category findParentByParentId(Integer parentId);

    String findLastInsertCodeByRootLevel(Integer rootLevel);

    String findLastInsertCodeByBranchAndLevel(@Param("branch") String branch,
                                              @Param("level") Integer level);

    List<Category> findAll();

    List<Category> findChildCategoriesOfSelectedCategory(@Param("branch") String branch,
                                                         @Param("startSearchCode") String startSearchCode);

    int deleteCategory(Integer id);

    int deleteCategoriesOfSelectedCategory(@Param("branch") String branch,
                                           @Param("startSearchCode") String startSearchCode);

    int updateCategoryName(@Param("id") Integer id,
                           @Param("newName") String newName);

    boolean existsById(Integer parentId);
}
