package com.example.musinsa.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private Integer id;
    private String branch;
    private String name;
    private String code;
    private Integer level;
}
