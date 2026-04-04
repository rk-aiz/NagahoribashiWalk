package com.example.nagahoribashi_walk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * サブカテゴリ管理用DTO
 *
 * @author 海津
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminSubCategoryRow {

    /** 主キー */
    private Long id;

    /** サブカテゴリ名 */
    private String name;

    /** 属するカテゴリのID */
    private Long categoryId;

    private boolean isDefault;

    /** Thymeleafから参照用 */
    public boolean getIsDefault() {
        return this.isDefault;
    }
}
