package com.example.nagahoribashi_walk.entity;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * @author 臼井
 */
@Data
public class Review {

	/** 主キー */
	private Long id;

	// 投稿したユーザーのID（users.id 参照）
	private Long userId;
	// 対象スポットのID（spots.id 参照）
	private Long spotId;
	// 5段階評価（1〜5の数値）
	private Integer rating;
	// 自由記述の口コミ本文
	private String comment;
	// 口コミの投稿日時
	private LocalDateTime createdAt;
	// 口コミの最終編集日時
	private LocalDateTime updatedAt;

}
