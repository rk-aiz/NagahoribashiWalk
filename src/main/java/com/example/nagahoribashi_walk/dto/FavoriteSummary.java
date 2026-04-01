package com.example.nagahoribashi_walk.dto;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * お気に入り一覧用のDTO
 * 
 * @author 海津
 */
@Data
public class FavoriteSummary {

    // スポットID。詳細ページへのリンク・削除操作（DELETE /favorite/{spotId}）に使用
    // s.id
    private Long spotId;

    // スポット名
    // s.spot_name
    private String spotName;

    // 代表画像URL。display_order が最小のものを取得
    // sp.photo_url
    private String photoUrl;

    // レビュー平均評価。レビュー0件の場合は null
    // AVG(r.rating)
    private Double averageRating;

    // カテゴリ名
    // c.name
    private String categoryName;

    // サブカテゴリ名
    // sc.name
    private String subCategoryName;

    // キーワード。カードのタグバッジ表示に使用
    // s.keywords
    private String keywords;

    // お気に入り登録日時
    // f.created_at
    private LocalDateTime favoritedAt;

}
