package com.example.nagahoribashi_walk.service;

import com.example.nagahoribashi_walk.entity.Review;

public interface ReviewService {

	/**
	 * レビュー情報を登録する
	 * 
	 * @param review レビュー情報
	 * @param username ログイン中のユーザー名
	 */
	void addReview(Review review, Long userId);

	/**
	 * レビューIDに対応するレビューを1件取得する
	 * 
	 * @param reviewId レビューID
	 * @return レビュー情報
	 */
	Review findById(Long reviewId);

	/**
	 * レビュー情報を更新する
	 * 
	 * @param review レビュー情報
	 * @param userId ログイン中のユーザーID
	 */
	void updateReview(Review review, Long userId);

}
