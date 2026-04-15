package com.example.nagahoribashi_walk.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 海津
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NavCategory {

    // 主キー
    private Long id;

    // カテゴリ名
    private String name;

    // 表示順
    private Integer displayOrder;

    // サブカテゴリ
    List<NavSubCategory> subCategories;

}
