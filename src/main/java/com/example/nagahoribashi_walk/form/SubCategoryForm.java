package com.example.nagahoribashi_walk.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * サブカテゴリ用Form
 */
@Data
public class SubCategoryForm {

    private boolean isNew;

    /** 主キー */
    private Long id;

    /** 親となるカテゴリのID（categories.id 参照） */
    private Long categoryId;

    /** サブカテゴリ名 */
    @NotBlank(message = "サブカテゴリ名を入力してください")
    @Size(max = 100, message = "サブカテゴリ名は100文字以内で入力してください")
    private String name;

    private Integer displayOrder;

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
