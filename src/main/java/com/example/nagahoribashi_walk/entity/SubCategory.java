package com.example.nagahoribashi_walk.entity;

import lombok.Data;

/**
 * サブカテゴリのエンティティ
 *
 * @author 篠原
 */
@Data
public class SubCategory {

    // 主キー
    private Long id;

    // 親となるカテゴリのID（categories.id 参照）
    private Long categoryId;

    // サブカテゴリ名
    private String name;

    // サブカテゴリの表示順
    private Integer displayOrder;

    // 「その他」扱いのカテゴリフラグ
    private boolean isDefault;
}
