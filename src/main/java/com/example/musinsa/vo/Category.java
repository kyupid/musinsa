package com.example.musinsa.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Builder
@ToString
public class Category {
    private Integer id;
    private String branch;
    private String name;

    @JsonIgnore
    private String code;

    private Integer level;
}
