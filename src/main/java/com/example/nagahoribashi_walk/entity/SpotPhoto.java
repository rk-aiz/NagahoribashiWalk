package com.example.nagahoribashi_walk.entity;

import lombok.Builder;
import lombok.Data;

/**
 * spot_photosテーブルに対応したEntity
 * 
 * @author 海津
 */
@Data
@Builder
public class SpotPhoto {
	
	// 主キー
	private Long id;
	
	// 画像が属するスポットのID（spots.id を参照する外部キー）
	private Long spotId;
	
	// 同一スポット内での画像表示順
	private Integer displayOrder;
	
	// 画像のURLまたは保存先パス
	private String photoUrl;

}
