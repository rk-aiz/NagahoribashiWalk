package com.example.nagahoribashi_walk.dto;

import java.util.List;

import lombok.Data;

/**
 * @author 海津
 */
@Data
public class NavCategory {

    /** 主キー */
    private Long id;

    /** カテゴリ名 */
    private String name;

    /** 表示順 */
    private Integer displayOrder;

    /** サブカテゴリ */
    List<NavSubCategory> subCategories;

}
