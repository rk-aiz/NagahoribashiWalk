package com.example.nagahoribashi_walk.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

/**
 * レビュー投稿フォーム
 *
 * @author 池田
 */
@Data
public class ReviewForm {

    /** ５段階評価 */
    @Min(value = 1, message = "☆ 1～5で評価してください")
    @Max(value = 5, message = "☆ 1～5で評価してください")
    @NotNull(message = "評価を選択してください")
    private Integer rating;

    /** レビュー本文 */
    @Size(max = 400, message = "レビューは400文字以内で入力してください")
    private String comment;
}
