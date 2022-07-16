package com.example.musinsa.dao;

import com.example.musinsa.vo.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CategoryDao {
    void createCategory(@Param("category") Category category);

    Category findById(Integer id);

    Category findParentByParentId(Integer parentId);

    String findLastInsertCodeByRootLevel(Integer rootLevel);

    String findLastInsertCodeByBranchAndLevel(@Param("branch") String branch,
                                              @Param("level") Integer level);
}
