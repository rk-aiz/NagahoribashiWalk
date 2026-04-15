package com.example.nagahoribashi_walk.dto;

import lombok.Data;

/**
 * @author 海津
 */
@Data
public class NavSubCategory {

    // 主キー
    private Long id;

    // サブカテゴリ名
    private String name;

    // 属するカテゴリのID
    private Long categoryId;

    // 表示順序
    private Integer displayOrder;

}
