package com.example.musinsa.dao;

import com.example.musinsa.vo.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryDao {
    void createCategory(Category category);

    Category findParentByParentId(Integer parentId);
}
