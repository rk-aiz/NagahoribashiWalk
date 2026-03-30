package com.example.nagahoribashi_walk.dto;

import lombok.Data;

/** 
 * @author 池田
 */

@Data
public class SpotSummary {

	//スポットID。詳細画面への遷移に使用する
	//s.id
	private Long id;

	//スポット名
	//s.spot_name
	private String spotName;

	//表示用の代表画像1件。spot_photos から display_order が最小のものを取得する想定
	//sp.photo_url
	private String photoUrl;

	//レビュー平均評価。レビューが0件の場合は null になる可能性がある
	//AVG(r.rating)
	private Double averageRating;

	//カテゴリ名
	//c.name
	private String categoryName;

	//サブカテゴリ名
	//sc.name
	private String subCategoryName;

	//スポットの特徴・検索用キーワード。一覧画面で概要表示として利用する
	//s.keywords
	private String keywords;

}
