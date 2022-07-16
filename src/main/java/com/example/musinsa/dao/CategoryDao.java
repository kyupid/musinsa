package com.example.musinsa.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryDao {
    void createRootCategory(String name);
}
