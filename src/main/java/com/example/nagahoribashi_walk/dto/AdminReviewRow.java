package com.example.nagahoribashi_walk.dto;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 管理者画面用レビュー要約
 *
 * @author 海津
 */
@Data
public class AdminReviewRow {

    // 主キー
    private Long id;

    // スポット名
    private String spotName;

    // ユーザー名
    private String userName;

    // 5段階評価
    private Integer rating;

    // コメント
    private String comment;

    // 投稿日時
    private LocalDateTime createdAt;

    private boolean isEnabled;
}
