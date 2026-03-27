package com.example.nagahoribashi_walk.entity;

import java.time.LocalDateTime;

import lombok.Data;
/**
 * 
 * @author 池田
 */
@Data
public class Spot {
	
	//主キー・自動採番
	private Long id;
	
	//スポットの正式名称。画面上のタイトルとして使用
	private String spotName;
	
	//sub_categoriesテーブルのID。未分類の場合はNULLを許容
	private Integer subCategoryId;
	
	//公式サイトのURL。https://から始まる文字列
	private String websiteUrl;
	
	//Googleマップの共有URL。埋め込み表示などに使用
	private String gmapUrl;
	
	//スポットの所在地
	private String address;
	
	//営業時間。自由テキスト入力
	private String businessHours;
	
	//定休日。テキスト形式
	private String closedDays;
	
	//予算の目安。「1,000円〜2,000円」のような表示用
	private String estimatedBudget;
	
	//検索用キーワード
	private String keywords;
	
	//スポットの詳細説明
	private String details;
	
	//論理削除日時
	private LocalDateTime deletedAt;
	
	//登録日時。システムで自動設定
	private LocalDateTime createdAt;
	
	//更新日時。データ更新時にシステムで自動設定
	private LocalDateTime updatedAt;
	
	

}
