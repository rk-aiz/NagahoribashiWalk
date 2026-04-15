package com.example.nagahoribashi_walk.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * レビュー投稿フォーム
 *
 * @author 池田
 */
@Data
public class ReviewForm {

    // 新規投稿かどうか（true=新規、false=編集）
    private boolean isNew;

    // レビューID（編集・削除時のみ使用）
    private Integer id;

    // 5段階評価
    @Min(value = 1, message = "☆ 1～5で評価してください")
    @Max(value = 5, message = "☆ 1～5で評価してください")
    @NotNull(message = "評価を選択してください")
    private Integer rating;

    // レビュー本文（400文字以内）
    @Size(max = 400, message = "レビューは400文字以内で入力してください")
    private String comment;

    // Thymeleafが正しくbooleanにアクセスする用
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
