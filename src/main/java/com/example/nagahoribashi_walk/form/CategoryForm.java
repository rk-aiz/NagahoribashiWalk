package com.example.nagahoribashi_walk.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * カテゴリ用Form
 */
@Data
public class CategoryForm {

    // 新規登録か編集を判定する
    private boolean isNew;

    // 主キー（新規登録の場合はNULL）
    private Long id;

    // カテゴリ名
    @NotBlank(message = "カテゴリ名を入力してください")
    @Size(max = 100, message = "カテゴリ名は100文字以内で入力してください")
    private String name;

    // 表示順序
    private Integer displayOrder;

    // カテゴリ色（#rrggbb 形式）
    @Pattern(regexp = "^#[0-9a-fA-F]{6}$", message = "色は #rrggbb 形式で入力してください")
    private String color;

    public boolean getIsNew() {
        return this.isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public void setNew(boolean isNew) {
        setIsNew(isNew);
    }
}
