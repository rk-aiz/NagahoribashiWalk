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

    private Long id;
    private String spotName;
    private String photoUrl;
    private String categoryName;
    private String subCategoryName;
    private Double averageRating;
    private Integer pvCount;
    private LocalDateTime updatedAt;
    private String categoryColor;
}
