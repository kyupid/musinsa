package com.example.musinsa.vo;

import lombok.*;

@Getter
@Builder
@ToString
public class Category {
    private Integer id;
    private String branch;
    private String name;
    private String code;
    private Integer level;
}
