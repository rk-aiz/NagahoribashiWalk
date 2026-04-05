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
    private long id;

    /** サブカテゴリ名 */
    private String name;

    /** 属するカテゴリのID */
    private long categoryId;

    /** 表示順序 */
    private int displayOrder;

    /** trueは未分類カテゴリ・フォールバック先・削除不可 */
    private boolean isDefault;

    /** Thymeleafから参照用 */
    public boolean getIsDefault() {
        return this.isDefault;
    }
}
