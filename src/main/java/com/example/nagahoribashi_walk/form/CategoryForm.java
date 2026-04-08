package com.example.nagahoribashi_walk.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * カテゴリ用Form
 */
@Data
public class CategoryForm {

    private boolean isNew;

    /** カテゴリ名 */
    @NotBlank(message = "カテゴリ名を入力してください")
    @Size(max = 100, message = "カテゴリ名は100文字以内で入力してください")
    private String name;

    public boolean getIsNew() {
        return this.isNew;
    }
}
