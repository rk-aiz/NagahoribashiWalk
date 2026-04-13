package com.example.nagahoribashi_walk.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 大谷
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    /** 主キー */
    private Long id;

    /** カテゴリ名 */
    private String name;

    /** 表示順 */
    private Integer displayOrder;

    /** 色 */
    private String color;

    /** 「その他」扱いのカテゴリフラグ */
    private boolean isDefault;
}
