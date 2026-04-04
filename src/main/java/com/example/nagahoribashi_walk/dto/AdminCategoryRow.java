package com.example.nagahoribashi_walk.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * カテゴリ管理用DTO
 *
 * @author 海津
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminCategoryRow {

    /** 主キー */
    private long id;

    /** カテゴリ名 */
    private String name;

    /** 表示順 */
    private int displayOrder;

    private boolean isDefault;

    private List<AdminSubCategoryRow> subCategories;

    /** Thymeleaf参照用 */
    public boolean getIsDefault() {
        return this.isDefault;
    }
}
