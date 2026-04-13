package com.example.nagahoribashi_walk.dto;

import java.util.List;

import lombok.Data;

/**
 * @author 池田
 */
@Data
public class SpotDetail {

    /** スポットID s.id */
    private Long id;

    /** 店舗名 s.spot_name */
    private String spotName;

    // 平均評価。レビュー0件時は null
    // AVG(r.rating)
    private Double averageRating;

    // 総レビュー件数。レビューがない場合は 0
    // COUNT(r.id)
    private Integer reviewCount;

    // スポット詳細説明
    // s.details
    private String details;

    // 営業時間
    // s.business_hours
    private String businessHours;

    // 予算目安
    // s.estimated_budget
    private String estimatedBudget;

    // 住所
    // s.address
    private String address;

    // Google Map URL
    // s.gmap_url
    private String gmapUrl;

    // 公式サイトURL
    // s.website_url
    private String websiteUrl;

    // 定休日
    // s.closed_days
    private String closedDays;

    // キーワード(カンマ区切り)
    private String keywords;

    // カテゴリ名
    // c.name
    private String categoryName;

    // サブカテゴリ名
    // sc.name
    private String subCategoryName;

    // カテゴリ色
    private String categoryColor;

    // 画像一覧
    private List<SpotPhotoDTO> images;

    // レビュー一覧。子DTOとして保持
    private List<ReviewDTO> reviews;

}
