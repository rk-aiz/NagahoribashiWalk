package com.example.nagahoribashi_walk.dto;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 管理者画面用スポット要約
 *
 * @author 海津
 */
@Data
public class AdminSpotRow {

    /** 主キー */
    private Long id;

    /* スポット名 */
    private String spotName;

    /** 画像URL */
    private String photoUrl;

    /** カテゴリ名 */
    private String categoryName;

    /** サブカテゴリ名 */
    private String subCategoryName;

    /** 平均評価 */
    private Double averageRating;

    /** 閲覧数 */
    private Integer pvCount;

    /** カテゴリ色 */
    private String categoryColor;

    /** 更新日時 */
    private LocalDateTime updatedAt;
}
