package com.example.musinsa.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Category {
    private Integer id;
    private Integer branch;
    private String name;
    private String code;
    private Integer level;
}
