package com.example.nagahoribashi_walk.form;

import lombok.Data;

/**
 * レビュー投稿フォーム
 */
@Data
public class ReviewForm {

    // ５段階評価
    private Integer rating;

    // レビュー本文
    private String comment;
}
